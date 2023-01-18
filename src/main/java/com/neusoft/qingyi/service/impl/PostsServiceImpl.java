package com.neusoft.qingyi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.qingyi.common.ErrorCode;
import com.neusoft.qingyi.myenum.ResponseCode;
import com.neusoft.qingyi.pojo.Posts;
import com.neusoft.qingyi.pojo.PostsImg;
import com.neusoft.qingyi.qingyiexception.QingYiException;
import com.neusoft.qingyi.service.PostsImgService;
import com.neusoft.qingyi.service.PostsService;
import com.neusoft.qingyi.mapper.PostsMapper;
import com.neusoft.qingyi.util.FileUtils;
import com.neusoft.qingyi.util.RedisKeyUtils;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 29600
 * @description 针对表【t_posts】的数据库操作Service实现
 * @createDate 2022-11-12 23:10:04
 */
@Service
@Slf4j
public class PostsServiceImpl extends ServiceImpl<PostsMapper, Posts>
        implements PostsService {

    @Resource
    private PostsImgService postsImgService;
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private PostsMapper postsMapper;

    private String postsLikeKeyPre = "posts_like_count::";

    @Value("${posts.file-path}")
    private String postsFilePath;

    @Value("${posts.file-root-folder}")
    private String rootFolder;

    @Value("${server.url}")
    private String serverUrl;

    @Value("${server.port}")
    private String serverPort;

    @Override
    public List<Posts> getPostsByPage(Map<String, Object> pageMap) {
        Integer currentPage = (Integer) pageMap.get("currentPage");
        Integer pageSize = (Integer) pageMap.get("pageSize");
        String openid = (String) pageMap.get("openid");
        String key = "allPosts";
        List<Posts> pageList = postsMapper.selectAll(openid);
        SessionCallback<List<?>> sessionCallback = new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                // 开启事务
                operations.multi();
                // 查询的时候将点赞数量放入redis缓存中
                for (Posts posts : pageList) {
                    String key = postsLikeKeyPre + posts.getPId();
                    Integer currentPostsLikeCount = posts.getCurrentPostsLikeCount();
                    if (currentPostsLikeCount == null) {
                        redisTemplate.opsForValue().set(key, 0);
                    } else {
                        redisTemplate.opsForValue().set(key, currentPostsLikeCount);
                    }
                }
                List<?> exec = operations.exec();
                return exec;
            }
        };
        redisTemplate.multi();
        // 提交事务
        List<?> execute = (List<?>) redisTemplate.execute(sessionCallback);
        if (execute == null || execute.size() == 0) {
            log.error("点赞/取消点赞失败");
            return null;
        }
        return pageList;
//        Map<Integer, Integer> postsLikeMap = pageList.stream().collect(Collectors.toMap(Posts::getPId, Posts::getCurrentPostsLikeCount));
//        redisTemplate.opsForValue().multiSet(postsLikeMap);
        // 如果键不存在，那么先添加缓存
//        if (!redisTemplate.hasKey(key)) {
//            List<Posts> posts = postsMapper.selectAll(openid);
//
//            if (posts != null) {
//                // 直接将所有数据存入缓存中，以后直接从缓存中分页查询即可
//                redisTemplate.opsForList().rightPushAll(key, posts);
//                // 设置一个过期时间
//                redisTemplate.expire(key, 3 * 60, TimeUnit.SECONDS);
//            }
//        }
        // 然后从缓存中分页读取数据
//        List<Posts> pageList = redisTemplate.opsForList().range(key, (currentPage - 1) * pageSize, (currentPage - 1) * pageSize + pageSize - 1);
//        return pageList;
    }

    /**
     * 添加帖子的方法（延时双删）
     * 1.添加之前将redis中对应的缓存清除
     * 2.再执行数据库更新操作
     * 3.在大概几秒的延迟后再执行缓存清除
     *
     * @param posts 帖子
     */
    @Override
    public Posts addPosts(Posts posts) {
        // 1.在进行写操作之前，先把缓存清除
        redisTemplate.delete("allPosts");
        // 2.执行数据库更新操作
        int res = postsMapper.insertPosts(posts);
        // 更新失败
        if (res <= 0) {
            return null;
        }
        // 4.延迟几秒后再次删除缓存
        try {
            Thread.sleep(500);
            redisTemplate.delete("allPosts");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Posts getPostsById(Integer pId) {
        return postsMapper.selectByPId(pId);
    }

    @Override
    public Posts getPostsDetails(Integer pId, String openid) {
        return postsMapper.selectPostsDetailsByPid(pId, openid);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean publicPosts(Posts posts, MultipartFile[] img) {
        // 先执行帖子写入操作
        if (posts == null) {
            throw new QingYiException(ErrorCode.PARAMS_ERROR);
        }
        // 执行帖子基本信息写入操作
        if (postsMapper.insert(posts) <= 0) {
            throw new QingYiException(ErrorCode.OPERATION_ERROR);
        }
        // 执行成功后，拿到pid和openid，进行帖子图片的操作
        int pid = posts.getPId();
        String openid = posts.getOpenid();
        // 首先判断存放帖子图片的指定文件夹是否存在
        if (!FileUtils.folderExists(postsFilePath)) {
            return false;
        }
        String folderPath = postsFilePath + "\\" + openid + "__" + pid + "\\";
        if (img != null && FileUtils.folderExists(folderPath)) {
            // 如果指定文件夹路径存在或者创建成功后，才执行写入操作
            try {
                // 循环写入图片
                for (MultipartFile file : img) {
                    String fileName = UUID.randomUUID() + ".png";
                    FileUtils.writePostsImg(folderPath + "/" + fileName, file.getInputStream());
                    String resUrl = serverUrl + ":" + serverPort + "/" + rootFolder + "/" + openid + "__" + pid + "/" + fileName;
                    // 将图片地址写入数据库
                    postsImgService.save(new PostsImg(pid, resUrl));
                }
            } catch (Exception e) {
                log.error("写入帖子图片失败");
                // 如果出现异常，那么递归删除掉新创建的文件夹
                FileUtils.deleteDir(folderPath);
                e.printStackTrace();
                return false;
            }
        } else {
            // 文件夹创建失败，抛出异常
            throw new QingYiException(ErrorCode.OPERATION_ERROR);
        }
        return true;
    }

    @Override
    public List<Posts> getPostsPage(Integer currentPage, Integer pageSize) {
        return postsMapper.selectPostsPage((currentPage - 1) * pageSize, pageSize);
    }
}




