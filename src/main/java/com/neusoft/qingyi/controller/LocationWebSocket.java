package com.neusoft.qingyi.controller;

import cn.hutool.json.JSONUtil;
import com.neusoft.qingyi.pojo.MiniUser;
import com.neusoft.qingyi.service.LocationService;
import com.neusoft.qingyi.util.RedisUtils;
import com.neusoft.qingyi.vo.MiniUserLocationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@ServerEndpoint("/location/{miniUserLocationVoJson}")
@Slf4j
public class LocationWebSocket {
    private Session session;

    private String openid;
    private static final CopyOnWriteArrayList<LocationWebSocket> locationWebSockets = new CopyOnWriteArrayList<>();

    private static final ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();

    private static LocationService locationService;

    private static StringRedisTemplate stringRedisTemplate;

    @Autowired
    private void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        LocationWebSocket.stringRedisTemplate = stringRedisTemplate;
    }

    @Autowired
    private void setLocationService(LocationService locationService) {
        LocationWebSocket.locationService = locationService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("miniUserLocationVoJson") String miniUserLocationVoJson) {
        MiniUserLocationVo miniUserLocationVo = JSONUtil.toBean("{" + miniUserLocationVoJson + "}", MiniUserLocationVo.class);
        MiniUser miniUser = JSONUtil.toBean(stringRedisTemplate.opsForValue().get(RedisUtils.LOCATION_MINIUSER + miniUserLocationVo.getOpenid()), MiniUser.class);
        miniUserLocationVo.setMiniUser(miniUser);
        // 将小程序用户添加至地址服务
        locationService.addUserLocation(miniUserLocationVo);
        try {
            this.session = session;
            this.openid = miniUserLocationVo.getOpenid();
            locationWebSockets.add(this);
            sessionPool.put(openid, session);
            log.info("[{}]用户连接到地址服务，当前连接总数为：{}", openid, locationWebSockets.size());
            // 广播附近的用户，告知用户上线了
            this.sendMessageToNearUser(miniUserLocationVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose() {
        try {
            locationWebSockets.remove(this);
            sessionPool.remove(this.openid);
            locationService.removeUserLocation(this.openid);
            log.info("[{}]用户从地址服务断开连接，当前连接总数为：{}", openid, locationWebSockets.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 给附近的人发送地理位置消息
     *
     * @param miniUserLocationVo 地理位置消息
     */
    public void sendMessageToNearUser(MiniUserLocationVo miniUserLocationVo) {
        // 上线的用户的经纬度
        double longitude = miniUserLocationVo.getPoints()[0];
        double latitude = miniUserLocationVo.getPoints()[1];
        // 拿到附近的人
        GeoResults<RedisGeoCommands.GeoLocation<String>> nearUserLocation = locationService.getNearUserLocation(longitude, latitude, 100);
        if (nearUserLocation != null) {
            for (GeoResult<RedisGeoCommands.GeoLocation<String>> locationGeoResult : nearUserLocation) {
                // 获取附近的人的openid
                String openid = locationGeoResult.getContent().getName();
                // 给附近的人发送消息
                try {
                    this.sendSinglePointOfMessage(openid, JSONUtil.toJsonStr(miniUserLocationVo));
                } catch (Exception e) {
                    log.error("给附近的人发送消息失败");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 发送定点消息
     *
     * @param openid      接收消息的openid
     * @param messageBody 发送的消息体
     */
    public void sendSinglePointOfMessage(String openid, String messageBody) {
        Session session = sessionPool.get(openid);
        if (session != null && session.isOpen()) {
            try {
                log.info("【websocket消息】 单点消息:" + messageBody);
                session.getAsyncRemote().sendText(messageBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送广播消息
     *
     * @param openidArr   要接收的openid队列
     * @param messageBody 发送的消息体
     */
    public void broadcastMessage(String[] openidArr, String messageBody) {
        for (String openid : openidArr) {
            // 给附近的人发送消息
            Session session = sessionPool.get(openid);
            if (session != null && session.isOpen()) {
                try {
                    log.info("发送消息给附近的[{}]", openid);
                    session.getAsyncRemote().sendText(messageBody);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
