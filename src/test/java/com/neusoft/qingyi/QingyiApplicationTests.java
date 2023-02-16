package com.neusoft.qingyi;

import com.alibaba.fastjson2.JSONObject;
import com.neusoft.qingyi.mapper.*;
import com.neusoft.qingyi.pojo.PostsComment;
import com.neusoft.qingyi.service.PostsCommentService;
import com.neusoft.qingyi.service.PostsLikeService;
import com.neusoft.qingyi.service.PostsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
class QingyiApplicationTests {

    @Resource
    private PostsCommentService postsCommentService;

    @Resource
    private MiniUserMapper miniUserMapper;

    @Resource
    private PostsImgMapper postsImgMapper;

    @Resource
    private PostsMapper postsMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private PostsService postsService;

    @Resource
    private PostsLikeService postsLikeService;

    @Resource
    private PostsLikeMapper postsLikeMapper;

    @Resource
    private PostsCommentMapper postsCommentMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void miniUserTest() {

    }

    @Test
    public void postsImgTest() {
    }

    @Test
    public void postsTest() {
        List allPosts = redisTemplate.opsForList().range("allPosts", 11, 20);
        System.out.println("allPosts = " + allPosts);
    }

    @Test
    public void test() {
//        String base64 = (String) redisTemplate.opsForList().rightPop("olG-q5aFDk6wc4tR446WUp3Gct1U:imgList");
//        System.out.println("base64 = " + base64);
//        Map<String, Integer> map = new HashMap<>();
//        map.put("currentPage", 1);
//        map.put("pageSize", 10);
//        List<Posts> postsByPage = postsService.getPostsByPage(map);
//        System.out.println("postsByPage = " + postsByPage);
//        redisTemplate.opsForHash().put("testHash","olG-q5aFDk6wc4tR446WUp3Gct1U::1",2);
    }

    @Test
    public void postLikeTest() {
//        for (int i = 1; i <= 10; i++) {
//            PostsLike postsLike = new PostsLike();
//            postsLike.setOpenid("omc0E5HPG5SlVKyyl9ttf9odzUIk");
//            postsLike.setP_id(i);
//            boolean res = postsLikeService.likeOrUnLikePosts(postsLike);
//            System.out.println("res = " + res);
//            System.out.println(redisTemplate.opsForHash().get(RedisKeyUtils.MAP_KEY_USER_LIKED, "omc0E5HPG5SlVKyyl9ttf9odzUIk::1"));
//        }
    }

    @Test
    public void postsCommentTest() {
        List<PostsComment> postsCommentList = postsCommentMapper.selectCommentByPid(1);
        System.out.println("postsCommentList = " + postsCommentList);
    }

    @Test
    public void webSocketTest() {
        Map<String,String> map = new HashMap<>();
        RecordId add = redisTemplate.opsForStream().add("123", map);
        System.out.println("add = " + add);
    }
}
