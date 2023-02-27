package com.neusoft.qingyi.service;

import com.neusoft.qingyi.pojo.Posts;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.qingyi.util.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author 29600
 * @description 针对表【t_posts】的数据库操作Service
 * @createDate 2022-11-12 23:10:04
 */
public interface PostsService extends IService<Posts> {

    Posts addPosts(Posts posts);

    Posts getPostsById(Integer pId);

    Posts getPostsDetails(Integer pId, String openid);

    boolean publicPosts(Posts posts, MultipartFile[] img);

    List<Posts> getPostsPage(Integer currentPage,Integer pageSize,String openid);

    ResponseResult<?> queryPosts(Long max, Integer offset);
}
