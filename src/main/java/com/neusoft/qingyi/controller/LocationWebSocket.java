package com.neusoft.qingyi.controller;

import cn.hutool.json.JSONUtil;
import com.neusoft.qingyi.vo.MiniUserLocationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@ServerEndpoint(value = "/location/{openid}")
@Slf4j
public class LocationWebSocket {

    private StringRedisTemplate stringRedisTemplate;
    private WebSocketSession webSocketSession;

    private String openid;
    private static final CopyOnWriteArrayList<LocationWebSocket> webSockets = new CopyOnWriteArrayList<>();

    private static final ConcurrentHashMap<String, WebSocketSession> webSocketSessionPool = new ConcurrentHashMap<>();

    @Autowired
    private StringRedisTemplate getStringRedisTemplate() {
        return new StringRedisTemplate();
    }

    @OnOpen
    public void onOpen(Session session, WebSocketSession webSocketSession, @PathVariable String openid) {
        Map<String, Object> objectMap = webSocketSession.getAttributes();
        MiniUserLocationVo miniUserLocationVo = (MiniUserLocationVo) objectMap.get("miniUserLocationVo");
        System.out.println(miniUserLocationVo);
        // 小程序用户通过WebSocket连接到地理位置服务
        try {
            this.webSocketSession = webSocketSession;
            this.openid = miniUserLocationVo.getOpenid();
            webSockets.add(this);
            webSocketSessionPool.put(openid, webSocketSession);
            log.info("【有新的用户连接到地址服务】(${})，当前连接总数为：${}", openid, webSockets.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose() {
        try {
            webSockets.remove(this);
            webSocketSessionPool.remove(this.openid);
            log.info("(${})用户断开连接，当前连接总数为：${}", openid, webSockets.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误,原因:" + error.getMessage());
        error.printStackTrace();
    }
}
