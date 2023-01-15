package com.neusoft.qingyi.listener;

import com.neusoft.qingyi.service.PostsLikeService;
import com.neusoft.qingyi.util.RedisKeyUtils;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Resource
    private PostsLikeService postsLikeService;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * 针对redis数据失效事件，进行数据处理
     *
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 用户做自己的业务处理即可,注意message.toString()可以获取失效的key
        String expiredKey = message.toString();
//        System.out.println("expiredKey = " + expiredKey);
//        if (expiredKey.equals(RedisKeyUtils.MAP_KEY_USER_LIKED)) {
//            // 如果是点赞key过期了，就上传数据库
//            postsLikeService.uploadLikeRedis();
//            System.out.println("点赞到期了，准备上传数据库");
//        }
    }
}