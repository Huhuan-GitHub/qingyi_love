package com.neusoft.qingyi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.qingyi.mapper.*;
import com.neusoft.qingyi.pojo.*;
import com.neusoft.qingyi.service.PostsCommentService;
import com.neusoft.qingyi.service.PostsLikeService;
import com.neusoft.qingyi.service.PostsService;
import com.neusoft.qingyi.util.RedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.ClassPath;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
}
