package com.neusoft.qingyi.controller;


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
        // 小程序用户连接到WebSocket时，将数据库中的消息读取出来，放入Redis中
        try {
            this.session = session;
            this.openid = openid;
            webSockets.add(this);
            sessionPool.put(openid, session);
            log.info("【websocket消息】有新的连接(" + openid + ")，总数为:" + webSockets.size());
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
        String msg = (String) jsonObject.get("messageContent");
        MiniUser sendMiniUser = JSONObject.parseObject(JSONObject.toJSONString(jsonObject.get("sendMiniUser")), MiniUser.class);
        MiniUser receiveMiniUser = JSONObject.parseObject(JSONObject.toJSONString(jsonObject.get("receiveMiniUser")), MiniUser.class);
        this.sendMessageToMiniUser(sendMiniUser, receiveMiniUser, msg);
        log.info("【websocket消息】收到客户端消息:" + message);
    }

    public void sendMessageToMiniUser(MiniUser sendMiniUser, MiniUser receiveMiniUser, String message) {
        String sendOpenid = sendMiniUser.getOpenid();
        String receiveOpenid = receiveMiniUser.getOpenid();
        String keyPrefix = sendOpenid + "::" + receiveOpenid;
        String keyPrefix2 = receiveOpenid + "::" + sendOpenid;
        MiniUserChatMessage miniUserChatMessage = new MiniUserChatMessage();
        miniUserChatMessage.setSendOpenid(sendOpenid);
        miniUserChatMessage.setReceiveOpenid(receiveOpenid);
        miniUserChatMessage.setMessageType(0);
        miniUserChatMessage.setMessageContent(message);
        miniUserChatMessage.setSendTime(new Date());
        miniUserChatMessage.setIsDeleted(0);
        miniUserChatMessage.setSendMiniUser(sendMiniUser);
        miniUserChatMessage.setReceiveMiniUser(receiveMiniUser);
        String trueKey = "";
        if (Boolean.TRUE.equals(redisTemplate.hasKey(keyPrefix + "::newMessage"))) {
            redisTemplate.opsForList().leftPush(keyPrefix + "::newMessage", miniUserChatMessage);
            trueKey = keyPrefix + "::newMessage";
        } else if (Boolean.TRUE.equals(redisTemplate.hasKey(keyPrefix2 + "::newMessage"))) {
            redisTemplate.opsForList().leftPush(keyPrefix2 + "::newMessage", miniUserChatMessage);
            trueKey = keyPrefix2 + "::newMessage";
        } else {
            trueKey = keyPrefix + "::newMessage";
            redisTemplate.opsForList().leftPush(keyPrefix + "::newMessage", miniUserChatMessage);
//            redisTemplate.opsForList().leftPush(keyPrefix + "::oldMessage", 0);
        }
        JSONObject jsonObject = new JSONObject();
        miniUserChatMessage.setUnRead(0L);
        jsonObject.put("messageBody", miniUserChatMessage);
        Session receiveSession = sessionPool.get(receiveOpenid);
        Session sendSession = sessionPool.get(sendOpenid);
        // 返回消息给发送者
        sendSession.getAsyncRemote().sendText(jsonObject.toJSONString());
        // 如果消息接收方在线，才发送给消息接收方
        if (receiveSession != null && receiveSession.isOpen()) {
            try {
                log.info("消息接收方在线，发送给：" + receiveMiniUser + "," + message);
                miniUserChatMessage.setUnRead(redisTemplate.opsForList().size(trueKey));
                jsonObject.put("messageBody", miniUserChatMessage);
                // 返回消息个接收者
                receiveSession.getAsyncRemote().sendText(jsonObject.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            log.warn("消息的接收方不在线！");
        }
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
