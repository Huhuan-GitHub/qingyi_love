package com.neusoft.qingyi.controller;


import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@ServerEndpoint("/websocket/{openid}")
@Slf4j
public class WebSocket {

    private Session session;

    private String openid;
    private static CopyOnWriteArrayList<WebSocket> webSockets = new CopyOnWriteArrayList<>();

    private static ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<String, Session>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "openid") String openid) {
        try {
            this.session = session;
            this.openid = openid;
            webSockets.add(this);
            sessionPool.put(openid, session);
            log.info("【websocket消息】有新的连接，总数为:" + webSockets.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            webSockets.remove(this);
            sessionPool.remove(this.openid);
            log.info("【websocket消息】连接断开，总数为:" + webSockets.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 收到客户端消息调用的方法
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject jsonObject = JSONObject.parseObject(message);
        String send_openid = (String) jsonObject.get("send_openid");
        String receive_openid = (String) jsonObject.get("receive_openid");
        String msg = (String) jsonObject.get("message");
//        this.sendOneMessage(receive_openid, msg);
        this.sendMessageToMiniUser(send_openid, receive_openid, msg);
        log.info("【websocket消息】收到客户端消息:" + message);
    }

    /**
     * 发送错误时的处理
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误,原因:" + error.getMessage());
        error.printStackTrace();
    }


    // 此为广播消息
    public void sendAllMessage(String message) {
        log.info("【websocket消息】广播消息:" + message);
        for (WebSocket webSocket : webSockets) {
            try {
                if (webSocket.session.isOpen()) {
                    webSocket.session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息
    public void sendOneMessage(String openid, String message) {
        Session session = sessionPool.get(openid);
        if (session != null && session.isOpen()) {
            try {
                log.info("【websocket消息】 单点消息:" + message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessageToMiniUser(String send_openid, String receive_openid, String message) {
        Session session = sessionPool.get(receive_openid);
        if (session != null && session.isOpen()) {
            try {
                log.info("【websocket消息】 单点消息:" + message);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("send_openid", send_openid);
                jsonObject.put("receive_openid", receive_openid);
                jsonObject.put("message", message);
                session.getAsyncRemote().sendText(jsonObject.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息(多人)
    public void sendMoreMessage(String[] openids, String message) {
        for (String userId : openids) {
            Session session = sessionPool.get(userId);
            if (session != null && session.isOpen()) {
                try {
                    log.info("【websocket消息】 单点消息:" + message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
