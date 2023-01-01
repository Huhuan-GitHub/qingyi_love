package com.neusoft.qingyi.service;

import com.neusoft.qingyi.pojo.PostsImg;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @author 29600
 * @description 针对表【t_posts_img】的数据库操作Service
 * @createDate 2022-11-12 23:10:04
 */
public interface PostsImgService extends IService<PostsImg> {
    int writeImgToCache(Map<String, Object> imgParams) throws IOException;

    int writeImgToCOS(Map<String, Object> imgParams);

    int addPostsImg(Integer pId, String openid, MultipartFile files) throws IOException;
}
