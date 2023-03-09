package com.neusoft.qingyi.controller;

import cn.hutool.json.JSONUtil;
import com.neusoft.qingyi.service.LocationService;
import com.neusoft.qingyi.vo.MiniUserLocationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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

    @Autowired
    private void setLocationService(LocationService locationService){
        LocationWebSocket.locationService = locationService;
    }
    @OnOpen
    public void onOpen(Session session, @PathParam("miniUserLocationVoJson") String miniUserLocationVoJson) {
        MiniUserLocationVo miniUserLocationVo = JSONUtil.toBean("{" + miniUserLocationVoJson + "}", MiniUserLocationVo.class);
        // 将小程序用户添加至地址服务
        locationService.addUserLocation(miniUserLocationVo);
        try {
            this.session = session;
            this.openid = miniUserLocationVo.getOpenid();
            locationWebSockets.add(this);
            sessionPool.put(openid, session);
            log.info("【{}】用户连接到地址服务，当前连接总数为：{}", openid, locationWebSockets.size());
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
            log.info("【{}】用户从地址服务断开连接，当前连接总数为：{}", openid, locationWebSockets.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
