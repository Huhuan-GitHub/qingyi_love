package com.neusoft.qingyi.service;

import com.neusoft.qingyi.pojo.PostsComment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 29600
* @description 针对表【t_posts_comment】的数据库操作Service
* @createDate 2023-01-01 21:34:56
*/
public interface PostsCommentService extends IService<PostsComment> {

    PostsComment miniUserCommentPosts(PostsComment postsComment);
}
