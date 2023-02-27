package com.neusoft.qingyi.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.qingyi.common.ErrorCode;
import com.neusoft.qingyi.common.ResultUtils;
import com.neusoft.qingyi.dto.ScrollResult;
import com.neusoft.qingyi.pojo.Posts;
import com.neusoft.qingyi.pojo.PostsImg;
import com.neusoft.qingyi.qingyiexception.QingYiException;
import com.neusoft.qingyi.service.PostsImgService;
import com.neusoft.qingyi.service.PostsService;
import com.neusoft.qingyi.mapper.PostsMapper;
import com.neusoft.qingyi.util.FileUtils;
import com.neusoft.qingyi.util.RedisUtils;
import com.neusoft.qingyi.util.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

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
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private PostsImgService postsImgService;
    @Resource
    private RedisTemplate<String, ?> redisTemplate;

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
        // 将pid写入redis，后续用于滚动分页
        stringRedisTemplate.opsForZSet().add(RedisUtils.POSTS_KEY_CACHE, String.valueOf(pid), new Date().getTime());
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
        } else if (img == null) {
            // 如果图片为空，不执行写入，直接返回数据库处理结果
            return true;
        } else {
            // 其他情况处理失败，抛出异常
            throw new QingYiException(ErrorCode.OPERATION_ERROR);
        }
        return true;
    }

    @Override
    public List<Posts> getPostsPage(Integer currentPage, Integer pageSize, String openid) {
        return postsMapper.selectPostsPage((currentPage - 1) * pageSize, pageSize, openid);
    }

    @Override
    public ResponseResult<?> queryPosts(Long max, Integer offset) {
        // 1.查询收件箱
        String key = "posts:";
        // 解析数据：p_id，minTime（时间戳）、offset
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet()
                .reverseRangeByScoreWithScores(key, 0, max, offset, 3);
        // 非空判断
        if (typedTuples == null || typedTuples.isEmpty()) {
            return ResultUtils.success();
        }
        ArrayList<Object> ids = new ArrayList<>(typedTuples.size());
        long miniTime = 0;
        int os = 1;
        for (ZSetOperations.TypedTuple<String> tuple : typedTuples) {
            // 获取p_id
            ids.add(Long.valueOf(tuple.getValue()));
            // 获取分数（时间戳）
            long time = tuple.getScore().longValue();
            if (time == miniTime) {
                os++;
            } else {
                miniTime = time;
                os = 1;
            }
        }
        // 根据p_id查询posts
        String idStr = StrUtil.join(",", ids);
        List<Posts> posts = new ArrayList<>(ids.size());
        for (Object id : ids) {
            Posts temp = postsMapper.selectPosts((Long) id);
            if (temp != null) {
                posts.add(temp);
            }
        }
//        List<Posts> posts = query().in("p_id", ids).last("ORDER BY FIELD(p_id," + idStr + ")").list();
        // 封装并返回
        ScrollResult r = new ScrollResult();
        r.setList(posts);
        r.setOffset(os);
        r.setMinTime(miniTime);
        return ResultUtils.success(r);
    }
}