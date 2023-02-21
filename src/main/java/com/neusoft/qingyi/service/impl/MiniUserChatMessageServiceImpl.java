package com.neusoft.qingyi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.qingyi.mapper.MiniUserChatMessageMapper;
import com.neusoft.qingyi.pojo.MiniUserChatMessage;
import com.neusoft.qingyi.service.MiniUserChatMessageService;
import com.neusoft.qingyi.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MiniUserChatMessageServiceImpl extends ServiceImpl<MiniUserChatMessageMapper, MiniUserChatMessage>
        implements MiniUserChatMessageService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private List<MiniUserChatMessage> chatMessages(Set<String> keys, String openid, Map<String, Integer> map) {
        List<MiniUserChatMessage> miniUserChatMessageList = new ArrayList<>();
        keys.forEach(key -> {
            String[] prefix = key.split("::");
            String sendOpenid = prefix[0];
            String receiveOpenid = prefix[1];
            String suffix = prefix[2];
            String tempKey = sendOpenid + "::" + receiveOpenid;
            String reverseTempKey = receiveOpenid + "::" + sendOpenid;
            String newMessage = tempKey + "::newMessage";
            String oldMessage = tempKey + "::oldMessage";
            if (!map.containsKey(tempKey) && !map.containsKey(reverseTempKey)) {
                MiniUserChatMessage miniUserChatMessage = null;
                if (suffix.equals("oldMessage")) {
                    miniUserChatMessage = (MiniUserChatMessage) redisTemplate.opsForList().index(newMessage, 0);
                    // 如果读取到了新消息，那么就不用读取旧消息了，用个Map来记录是否已经读取新消息
                    if (miniUserChatMessage == null) {
                        miniUserChatMessage = (MiniUserChatMessage) redisTemplate.opsForList().index(oldMessage, 0);
                    }
                }
                if (suffix.equals("newMessage")) {
                    miniUserChatMessage = (MiniUserChatMessage) redisTemplate.opsForList().index(newMessage, 0);
                }
                // 用于表示未读消息的数量，只有该通道不是发送者创建的时候，才未读数量
                if (!sendOpenid.equals(openid)) {
                    assert miniUserChatMessage != null;
                    miniUserChatMessage.setUnRead(redisTemplate.opsForList().size(newMessage));
                }
                map.put(tempKey, 1);
                miniUserChatMessageList.add(miniUserChatMessage);
            }
        });
        return miniUserChatMessageList;
    }

    @Override
    public synchronized List<MiniUserChatMessage> getMiniUserChatMessageList(String openid) {
        // 匹配发送者
        Set<String> sendKeys = RedisUtils.scan(openid + "*");
        // 匹配接收者
        Set<String> receiveKeys = RedisUtils.scan("*::" + openid + "::*");
        Map<String, Integer> map = new HashMap<>();
        List<MiniUserChatMessage> result = new ArrayList<>();
        result.addAll(chatMessages(sendKeys, openid, map));
        result.addAll(chatMessages(receiveKeys, openid, map));
        return result;
    }


    @Override
    public long sendMessageToMiniUser(String sendOpenid, String receiveOpenid, String message) {
        return 0;
    }

    /**
     * 缓存中中读取聊天消息
     *
     * @param sendOpenid    消息发送者
     * @param receiveOpenid 消息接收者
     * @return 消息列表
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<MiniUserChatMessage> viewMessage(String sendOpenid, String receiveOpenid) {
        String prefixKey = sendOpenid + "::" + receiveOpenid;
        Set<String> keys = null;
        keys = RedisUtils.scan(prefixKey + "*");
        if (keys == null || keys.isEmpty()) {
            prefixKey = receiveOpenid + "::" + sendOpenid;
        }
        String oldMessageKey = prefixKey + "::oldMessage";
        String newMessageKey = prefixKey + "::newMessage";
        List<MiniUserChatMessage> res = new ArrayList<>();
        // 1.先查询旧消息（oldMessage）
        MiniUserChatMessage oldMessage = null;
        List<MiniUserChatMessage> oldMessageList = new ArrayList<>();
        Long size = redisTemplate.opsForList().size(oldMessageKey);
        if (size != null) {
            for (int i = 0; i < size; i++) {
                oldMessageList.add((MiniUserChatMessage) redisTemplate.opsForList().index(oldMessageKey, i));
            }
        }
//        while ((oldMessage = (MiniUserChatMessage) redisTemplate.opsForList().rightPop(oldMessageKey)) != null) {
//            oldMessageList.add(oldMessage);
//        }
        // 2.再查询新消息（newMessage）
        MiniUserChatMessage newMessage = null;
        List<MiniUserChatMessage> newMessageList = new ArrayList<>();
        while ((newMessage = (MiniUserChatMessage) redisTemplate.opsForList().rightPop(newMessageKey)) != null) {
            newMessageList.add(newMessage);
            // 2.1查询出新消息后，消息已读，将新消息放入旧消息队列中
            redisTemplate.opsForList().leftPush(oldMessageKey, newMessage);
        }
        res.addAll(oldMessageList);
        res.addAll(newMessageList);
        // 3.2数据持久化（选做），将消息保存到Mysql中
        return res;
    }
}
