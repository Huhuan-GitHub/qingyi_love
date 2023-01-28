package com.neusoft.qingyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.qingyi.mapper.PostsLikeCountMapper;
import com.neusoft.qingyi.pojo.PostsLike;
import com.neusoft.qingyi.pojo.PostsLikeCount;
import com.neusoft.qingyi.service.PostsLikeService;
import com.neusoft.qingyi.mapper.PostsLikeMapper;
import com.neusoft.qingyi.util.PostsLikeEnum;
import com.neusoft.qingyi.util.RedisKeyUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author 29600
 * @description 针对表【t_posts_like】的数据库操作Service实现
 * @createDate 2022-11-12 23:10:04
 */
@Service
public class PostsLikeServiceImpl extends ServiceImpl<PostsLikeMapper, PostsLike>
        implements PostsLikeService {

    @Resource
    private PostsLikeMapper postsLikeMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private PostsLikeCountMapper postsLikeCountMapper;

    private String postsLikeKeyPre = "posts_like_count::";

    /**
     * 点赞/取消点赞帖子
     *
     * @param postsLike 帖子点赞对象
     * @return 处理结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer likeOrUnLikePosts(PostsLike postsLike) {
        Integer status = postsLike.getStatus();
        // 未点赞
        if (status == null || status == 0) {

        }
        return 0;
    }

    /**
     * 将点赞和点赞数量缓存上传数据库
     */
//    @Scheduled(fixedRate = RedisKeyUtils.UPDATE_TIME)
    @Transactional
    @Override
    public void uploadLikeRedis() {
        Set<String> keys = redisTemplate.opsForHash().keys(RedisKeyUtils.MAP_KEY_USER_LIKED);
        // 用来记录是否所有都上传成功
        boolean uploadResult = true;
        for (String key : keys) {
            // 构造参数
            String[] openid_pId = RedisKeyUtils.parseLikedKey(key);
            String openid = openid_pId[0];
            Integer pId = Integer.valueOf(openid_pId[1]);
            Integer status = (Integer) redisTemplate.opsForHash().get(RedisKeyUtils.MAP_KEY_USER_LIKED, key);
            PostsLike uploadPostsLike = new PostsLike();
            uploadPostsLike.setOpenid(openid);
            uploadPostsLike.setP_id(pId);
            uploadPostsLike.setStatus(status);
            PostsLike postsLike = postsLikeMapper.selectOne(new QueryWrapper<PostsLike>().eq("openid", openid).eq("p_id", pId));
            int insert_update_res;
            // 如果等于null，说明没有用户未点赞喜喜，执行插入操作
            if (postsLike == null) {
                insert_update_res = postsLikeMapper.insert(uploadPostsLike);
            } else {
                // 否则执行更新操作
                insert_update_res = postsLikeMapper.update(uploadPostsLike, null);
            }
            // 出现错误，抛出异常，事务回滚
            if (insert_update_res <= 0) {
                uploadResult = false;
                throw new RuntimeException("将缓存内的点赞数据写入数据库失败！");
            }
        }
        // 更新数据库里的点赞数
        Set<String> postsLikeCountKeySet = redisTemplate.keys(postsLikeKeyPre + "*");
        for (String key : postsLikeCountKeySet) {
            Integer count = (Integer) redisTemplate.opsForValue().get(key);
            String[] postsKeys = RedisKeyUtils.parseLikedKey(key);
            Integer posts_id = Integer.valueOf(postsKeys[1]);
            PostsLikeCount postsLikeCount = postsLikeCountMapper.selectOne(new QueryWrapper<PostsLikeCount>().eq("p_id", posts_id));
            // 如果为空，表示点赞数量统计表中没有点赞数据，就执行插入操作
            if (postsLikeCount == null) {
                postsLikeCount = new PostsLikeCount();
                postsLikeCount.setPId(posts_id);
                postsLikeCount.setCount(count);
                postsLikeCountMapper.insert(postsLikeCount);
                redisTemplate.opsForValue().set(key, postsLikeCount.getCount());
                break;
            }
            // 查询到有点赞记录，执行更新操作
            postsLikeCountMapper.update(postsLikeCount, null);
            redisTemplate.opsForValue().set(key, count);
        }
        if (uploadResult) {
            // 如果全部上传成功，就删除缓存
            redisTemplate.delete(RedisKeyUtils.MAP_KEY_USER_LIKED);
        }
    }

    /**
     * 点赞/取消点赞
     *
     * @param postsLike 帖子点赞对象
     */
    @Override
    public void likePost(PostsLike postsLike) {
    }

    /**
     * 点赞帖子的方法
     *
     * @param postsLike 点赞帖子对象
     */
    private void likePosts(PostsLike postsLike) {
        String userLikeKey = postsLike.getOpenid() + "::" + postsLike.getP_id();
        // 帖子主键
        String postsKey = postsLikeKeyPre + postsLike.getP_id();
        // 开启事务支持
        redisTemplate.setEnableTransactionSupport(true);
        // 开启事务
        redisTemplate.multi();
        redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_USER_LIKED, userLikeKey, 1);
        // 点赞数+1
        redisTemplate.opsForValue().increment(postsKey, 1);
        // 执行事务
        redisTemplate.exec();
//        redisTemplate.opsForHash().increment(RedisKeyUtils.MAP_KEY_USER_LIKED, postsId, 1);
    }

    /**
     * 取消帖子点赞的方法
     *
     * @param postsLike 点赞帖子对象
     */
    private void unLikePosts(PostsLike postsLike) {
        String userLikeKey = postsLike.getOpenid() + "::" + postsLike.getP_id();
        // 帖子主键
        String postsKey = postsLikeKeyPre + postsLike.getP_id();
        // 开启事务支持
        redisTemplate.setEnableTransactionSupport(true);
        // 开启事务
        redisTemplate.multi();
        redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_USER_LIKED, userLikeKey, 0);
        // 点赞数量-1
        redisTemplate.opsForValue().increment(postsKey, -1);
        // 执行事务
        redisTemplate.exec();
    }

    /**
     * 获取用户对某条帖子的点赞状态
     *
     * @param openid 用户唯一标识符
     * @param pid    帖子主键
     * @return 点赞状态
     */
    private Object likeStatus(String openid, Integer pid) {
        String likedKey = openid + "::" + pid;
        // 如果redis存在该点赞信息，则直接从缓存中读取
        if (redisTemplate.opsForHash().hasKey(RedisKeyUtils.MAP_KEY_USER_LIKED, likedKey)) {
            String status = redisTemplate.opsForHash().get(RedisKeyUtils.MAP_KEY_USER_LIKED, likedKey).toString();
            if (status.equals("1")) {
                return PostsLikeEnum.LIKE;
            }
            if (status.equals("0")) {
                return PostsLikeEnum.UNLIKE;
            }
        }
        // 缓存中没有，有可能是缓存被清除了，则从数据库中查询
        PostsLike postsLike = postsLikeMapper.selectOne(new QueryWrapper<PostsLike>().eq("openid", openid).eq("p_id", pid));
        if (postsLike == null) {
            // 如果没有查询到，就说明没有点赞该帖子，添加未点赞数据到数据库
//            PostsLike newPostsLike = new PostsLike();
//            newPostsLike.setOpenid(openid);
//            newPostsLike.setPId(pid);
//            newPostsLike.setStatus(1);
//            postsLikeMapper.insert(newPostsLike);
            // 点赞后，返回未点赞
            return PostsLikeEnum.UNLIKE;
        }
        if (postsLike.getStatus() == 1) {
            // 已点赞
            return PostsLikeEnum.LIKE;
        }
        if (postsLike.getStatus() == 0) {
            // 未点赞
            return PostsLikeEnum.UNLIKE;
        }
        return "";
    }
}




