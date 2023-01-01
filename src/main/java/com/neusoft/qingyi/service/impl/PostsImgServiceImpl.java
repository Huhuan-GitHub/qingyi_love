package com.neusoft.qingyi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.qingyi.pojo.PostsImg;
import com.neusoft.qingyi.service.PostsImgService;
import com.neusoft.qingyi.mapper.PostsImgMapper;
import com.neusoft.qingyi.util.CosUtil;
import com.qcloud.cos.model.UploadResult;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 29600
 * @description 针对表【t_posts_img】的数据库操作Service实现
 * @createDate 2022-11-12 23:10:04
 */
@Service
public class PostsImgServiceImpl extends ServiceImpl<PostsImgMapper, PostsImg>
        implements PostsImgService {

    @Resource
    private PostsImgMapper postsImgMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 将图片以流的方式写入缓存并设置过期时间
     *
     * @param imgParams 图片参数 openid,file
     * @return 写入结果
     */
    @Override
    public int writeImgToCache(Map<String, Object> imgParams) {
        String openid = (String) imgParams.get("openid");
        MultipartFile file = (MultipartFile) imgParams.get("file");
        Integer index = (Integer) imgParams.get("index");
        String key = openid + ":imgList:" + index;
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream finalInputStream = inputStream;
        // 写入redis缓存
        SessionCallback<Object> callback = new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                // 直接添加进redis缓存中
                operations.opsForList().leftPush(key, finalInputStream);
                // 设置过期时间为300s(5分钟)
                operations.expire(key, 5 * 60, TimeUnit.SECONDS);
                return operations.exec();
            }
        };
        System.out.println(redisTemplate.execute(callback));
        return 1;
    }

    /**
     * 将图片上传到腾讯云COS
     *
     * @param imgParams 图片参数 openid,file
     * @return 上传结果
     */
    @Override
    public synchronized int writeImgToCOS(Map<String, Object> imgParams) {
        return 0;
    }

    /**
     * 添加图片到数据库和腾讯云COS
     * key使用UUID+openid+Pid减少重复
     *
     * @param pId    帖子主键
     * @param openid 用户唯一标识符
     * @param files  上传文件
     * @return 上传结果
     */
    @Override
    public synchronized int addPostsImg(Integer pId, String openid, MultipartFile files) {
        // 随机生成一个UUID
        String UUID = java.util.UUID.randomUUID().toString();
        // 拼接一个key，上传图片的唯一标识
        String imgKey = UUID + openid + pId + ".png";
        // 上传结果，返回结果为URL地址
        String url;
        // 从files转换为流，为准备上传腾讯云做准备
        try {
            byte[] bytes = files.getBytes();
            InputStream in = new ByteArrayInputStream(bytes);
//            InputStream in = files.getInputStream();
            // 上传结果
            url = CosUtil.uploadFileByStream(in, imgKey);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("url = " + url);
        // 上传失败，抛出异常
        if (url == null) {
            throw new RuntimeException("图片上传腾讯云失败");
        }
        // 上传成功，将返回的对象链接存入数据库中
        String resultKey = url;
        // 存入数据库
        PostsImg postsImg = new PostsImg();
        postsImg.setPId(pId);
        postsImg.setImgUrl(resultKey);
        return postsImgMapper.insert(postsImg);
    }
}




