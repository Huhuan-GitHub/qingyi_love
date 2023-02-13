package com.neusoft.qingyi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.qingyi.pojo.PostsLikeCount;
import org.apache.ibatis.annotations.Param;

/**
* @author 29600
* @description 针对表【t_posts_like_count】的数据库操作Mapper
* @createDate 2022-12-30 19:21:03
* @Entity com.neusoft.qingyi.pojo.PostsLikeCount
*/
public interface PostsLikeCountMapper extends BaseMapper<PostsLikeCount> {
    Integer selectPostsLikeCount(@Param("pId") Integer pId);
}




