package com.neusoft.qingyi.service;

import com.neusoft.qingyi.pojo.PostsLike;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 29600
 * @description 针对表【t_posts_like】的数据库操作Service
 * @createDate 2022-11-12 23:10:04
 */
public interface PostsLikeService extends IService<PostsLike> {

    Integer likeOrUnLikePosts(PostsLike postsLike);

    void uploadLikeRedis();

    void likePost(PostsLike postsLike);
}
