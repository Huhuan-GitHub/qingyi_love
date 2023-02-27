package com.neusoft.qingyi.util;

import com.neusoft.qingyi.controller.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class RedisUtils {
    public static final String MAP_KEY_USER_LIKED = "MAP_KEY_USER_LIKED";
    // 每2min更新一次数据库
    public static final long UPDATE_TIME = 2 * 60 * 1000;
    // 缓存中能容纳的最大点赞数，超过这个阈值，就会清空点赞缓存，将数据上传至数据库
    public static final Integer MAX_LIKE_SIZE = 10;

    public static final String POSTS_COMMENT_KEY = "posts::comment::root";

    public static final String PUBLIC_POSTS_IMG_AUTH = "public::posts::img::auth";

    public static final String POSTS_KEY_CACHE = "posts:";

    public static final String USER_ATTENTION_PREFIX = "ATTENTION::";

    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisUtils.redisTemplate = redisTemplate;
    }

    /**
     * 拼接点赞人的openid和他点赞贴子主键作为redis的键key，例如：olG-q5aFDk6wc4tR446WUp3Gct1U::1
     *
     * @param openid 点赞人唯一标识符
     * @param pid    帖子主键
     * @return 拼接后的键
     */
    public static String getLikedKey(String openid, String pid) {
        return openid +
                "::" +
                pid;
    }

    /**
     * 将redis的键转换为openid和pid
     * 例如：
     * olG-q5aFDk6wc4tR446WUp3Gct1U::1 =>{"openid":"olG-q5aFDk6wc4tR446WUp3Gct1U","pId":1}
     *
     * @param likedKey 在redis中从键
     * @return 转换结果Map映射
     */
    public static String[] parseLikedKey(String likedKey) {
        return likedKey.split("::");
    }

    public static Set<String> scan(String matchKey) {
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keysTmp = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match(matchKey).count(1000).build());
            while (cursor.hasNext()) {
                keysTmp.add(new String(cursor.next()));
            }
            return keysTmp;
        });
    }
}
