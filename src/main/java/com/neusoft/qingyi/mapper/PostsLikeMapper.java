package com.neusoft.qingyi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.qingyi.pojo.PostsLike;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 29600
 * @description 针对表【t_posts_like】的数据库操作Mapper
 * @createDate 2022-11-12 23:10:04
 * @Entity com.neusoft.qingyi.pojo.PostsLike
 */
public interface PostsLikeMapper extends BaseMapper<PostsLike> {
    List<PostsLike> selectByOpenid(@Param("openid") String openid);

    Integer selectPostsLikeCount(@Param("pId") Integer pId);
}




