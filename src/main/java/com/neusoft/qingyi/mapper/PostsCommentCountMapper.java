package com.neusoft.qingyi.mapper;

import com.neusoft.qingyi.pojo.PostsCommentCount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author 29600
* @description 针对表【t_posts_comment_count】的数据库操作Mapper
* @createDate 2022-12-30 20:53:29
* @Entity com.neusoft.qingyi.pojo.PostsCommentCount
*/
public interface PostsCommentCountMapper extends BaseMapper<PostsCommentCount> {
    Integer selectPostsCommentCount(@Param("pId") Integer pId);
}




