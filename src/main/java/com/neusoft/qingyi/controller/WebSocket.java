package com.neusoft.qingyi.controller;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.neusoft.qingyi.pojo.MiniUser;
import com.neusoft.qingyi.pojo.MiniUserChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@ServerEndpoint("/websocket/{openid}")
@Slf4j
public class WebSocket {

    private static RedisTemplate<String, Object> redisTemplate;

    private Session session;

    private String openid;
    private static final CopyOnWriteArrayList<WebSocket> webSockets = new CopyOnWriteArrayList<>();

    private static final ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<String, Session>();

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        WebSocket.redisTemplate = redisTemplate;
    }

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
//            // 连接断开时，将Redis消息队列中的消息缓存写入数据库
//            Set<String> keys = redisTemplate.keys("*" + this.openid + "*");
//            assert keys != null;
//            // 遍历集合
//            keys.forEach((key) -> {
//                for (int i = 0; i < redisTemplate.opsForList().size(key); i++) {
//                    System.out.println((MiniUserChatMessage) redisTemplate.opsForList().rightPop(key));
//                }
//                redisTemplate.delete(key);
//            });
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
//        String send_openid = (String) jsonObject.get("send_openid");
//        String receive_openid = (String) jsonObject.get("receive_openid");
        String msg = (String) jsonObject.get("messageContent");
        MiniUser sendMiniUser = JSON.parseObject((String) jsonObject.get("sendMiniUser"), MiniUser.class);
        MiniUser receiveMiniUser = JSON.parseObject((String) jsonObject.get("receiveMiniUser"), MiniUser.class);
        this.sendMessageToMiniUser(sendMiniUser, receiveMiniUser, msg);
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

    public void sendMessageToMiniUser(MiniUser sendMiniUser, MiniUser receiveMiniUser, String message) {
        Session session = sessionPool.get(receiveMiniUser.getOpenid());
        // TODO:这里的消息类型字段暂时设置为0（文本类型）
        MiniUserChatMessage miniUserChatMessage = new MiniUserChatMessage(null, sendMiniUser.getOpenid(), receiveMiniUser.getOpenid(), 0, message, new Date(), 0, sendMiniUser, receiveMiniUser, null);
        String key = sendMiniUser.getOpenid() + "::" + receiveMiniUser.getOpenid();
        log.info("对方不在线，直接将消息写入Redis队列");
        // 直接将消息放入消息队列，断开连接时才将消息队列数据写入数据库
        redisTemplate.opsForList().leftPush(key, miniUserChatMessage);
        // 如果接收消息的用户在线的话，那么直接发送消息
        if (session != null && session.isOpen()) {
            try {
                log.info("消息接收方在线，发送给：" + receiveMiniUser + "," + message);
                JSONObject jsonObject = new JSONObject();
                miniUserChatMessage.setUnRead(redisTemplate.opsForList().size(key));
                jsonObject.put("messageBody", miniUserChatMessage);
                sessionPool.get(sendMiniUser.getOpenid()).getAsyncRemote().sendText(jsonObject.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            log.warn("消息的接收方不在线！");
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
