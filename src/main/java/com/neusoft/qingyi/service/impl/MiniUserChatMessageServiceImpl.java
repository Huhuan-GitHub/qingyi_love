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

@Service
@Slf4j
public class MiniUserChatMessageServiceImpl extends ServiceImpl<MiniUserChatMessageMapper, MiniUserChatMessage>
        implements MiniUserChatMessageService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private MiniUserChatMessageMapper miniUserChatMessageMapper;

    private List<MiniUserChatMessage> chatMessages(Set<String> keys, String openid) {
        Map<String, Integer> map = new HashMap<>();
        List<MiniUserChatMessage> miniUserChatMessageList = new ArrayList<>();
        keys.forEach(key -> {
            String[] prefix = key.split("::");
            String sendOpenid = prefix[0];
            String receiveOpenid = prefix[1];
            String suffix = prefix[2];
            String tempKey = sendOpenid + "::" + receiveOpenid;
            String newMessage = tempKey + "::newMessage";
            String oldMessage = tempKey + "::oldMessage";
            if (!map.containsKey(tempKey)) {
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
        List<MiniUserChatMessage> result = new ArrayList<>();
        result.addAll(chatMessages(sendKeys, openid));
        result.addAll(chatMessages(receiveKeys, openid));
        return result;
    }


    @Override
    public long sendMessageToMiniUser(String sendOpenid, String receiveOpenid, String message) {
        return 0;
        // TODO:这里的消息类型字段暂时设置为0（文本类型）
//        MiniUserChatMessage miniUserChatMessage = new MiniUserChatMessage(null, sendOpenid, receiveOpenid, 0, message, new Date(), 0, null, null);
//        String key = sendOpenid + "::" + receiveOpenid;
//        log.info("对方不在线，直接将消息写入Redis队列");
//        // 直接将消息放入消息队列，断开连接时才将消息队列数据写入数据库
//        return redisTemplate.opsForList().leftPush(key, miniUserChatMessage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<MiniUserChatMessage> getMiniUserMessage(String sendOpenid, String receiveOpenid) {
        return new ArrayList<>();
//        redisTemplate.setEnableTransactionSupport(true);
//        redisTemplate.multi();
//        String key = sendOpenid + "::" + receiveOpenid;
//        long size = redisTemplate.opsForList().size(key);
//        // 将redis数据写入Redis
//        for (int i = 0; i < size; i++) {
//            if (miniUserChatMessageMapper.insert((MiniUserChatMessage) redisTemplate.opsForList().index(key, i)) <= 0) {
//                throw new QingYiException(ErrorCode.OPERATION_ERROR);
//            }
//        }
//        // 消息已读，从Redis中删除数据
//        redisTemplate.delete(key);
//        redisTemplate.exec();
//        return miniUserChatMessageMapper.selectList(new QueryWrapper<MiniUserChatMessage>()
//                .eq("send_openid", sendOpenid)
//                .or()
//                .eq("send_openid", receiveOpenid)
//                .or()
//                .eq("receive_openid", sendOpenid)
//                .or()
//                .eq("receive_openid", receiveOpenid).orderByAsc("send_time"));
    }
}
