package com.neusoft.qingyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.qingyi.common.ErrorCode;
import com.neusoft.qingyi.mapper.MiniUserChatMessageMapper;
import com.neusoft.qingyi.pojo.MiniUserChatMessage;
import com.neusoft.qingyi.qingyiexception.QingYiException;
import com.neusoft.qingyi.service.MiniUserChatMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class MiniUserChatMessageServiceImpl extends ServiceImpl<MiniUserChatMessageMapper, MiniUserChatMessage>
        implements MiniUserChatMessageService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private MiniUserChatMessageMapper miniUserChatMessageMapper;

    @Override
    public synchronized List<MiniUserChatMessage> getMiniUserChatMessageList(String openid) {
        Set<String> keys = redisTemplate.keys("*" + openid);
        Set<MiniUserChatMessage> userMessageSet = new HashSet<>();
        // 如果从Redis中读取消息为空，没有最新消息，直接从数据库读
        if (keys == null) {
            new Thread(() -> {

            }).start();
        } else {
            keys.forEach((key) -> {
                MiniUserChatMessage newMessage = (MiniUserChatMessage) redisTemplate.opsForList().leftPop(key);
                redisTemplate.opsForList().leftPush(key, newMessage);
                newMessage.setUnRead(redisTemplate.opsForList().size(key));
                userMessageSet.add(newMessage);
            });
        }
        List<MiniUserChatMessage> miniUserChatMessageList = miniUserChatMessageMapper.selectMiniUserChatMessageList(openid);
        for (MiniUserChatMessage miniUserChatMessage : miniUserChatMessageList) {
            String sendOpenid = miniUserChatMessage.getSendOpenid();
            String receiveOpenid = miniUserChatMessage.getReceiveOpenid();
            AtomicBoolean isSameData = new AtomicBoolean(false);
            userMessageSet.forEach(item -> {
                if ((Objects.equals(sendOpenid, item.getSendOpenid()) || Objects.equals(sendOpenid, item.getReceiveOpenid())) && (Objects.equals(receiveOpenid, item.getSendOpenid()) || Objects.equals(receiveOpenid, item.getReceiveOpenid()))) {
                    isSameData.set(true);
                }
            });
            // 如果不包含相同数据
            if (!isSameData.get()) {
                userMessageSet.add(miniUserChatMessage);
            }
        }

        return new ArrayList<>(userMessageSet);
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
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.multi();
        String key = sendOpenid + "::" + receiveOpenid;
        long size = redisTemplate.opsForList().size(key);
        // 将redis数据写入Redis
        for (int i = 0; i < size; i++) {
            if (miniUserChatMessageMapper.insert((MiniUserChatMessage) redisTemplate.opsForList().index(key, i)) <= 0) {
                throw new QingYiException(ErrorCode.OPERATION_ERROR);
            }
        }
        // 消息已读，从Redis中删除数据
        redisTemplate.delete(key);
        redisTemplate.exec();
        return miniUserChatMessageMapper.selectList(new QueryWrapper<MiniUserChatMessage>()
                .eq("send_openid", sendOpenid)
                .or()
                .eq("send_openid", receiveOpenid)
                .or()
                .eq("receive_openid", sendOpenid)
                .or()
                .eq("receive_openid", receiveOpenid).orderByAsc("send_time"));
    }
}
