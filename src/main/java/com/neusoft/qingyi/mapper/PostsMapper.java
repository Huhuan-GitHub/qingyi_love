package com.neusoft.qingyi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.qingyi.pojo.Posts;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 29600
 * @description 针对表【t_posts】的数据库操作Mapper
 * @createDate 2022-11-12 23:10:04
 * @Entity com.neusoft.qingyi.pojo.Posts
 */
public interface PostsMapper extends BaseMapper<Posts> {
    List<Posts> selectAll(@Param("openid") String openid);

    int insertPosts(@Param("posts") Posts posts);

    Posts selectPosts(@Param("pId") Long pId);

    /**
     * 根据帖子主键获取帖子详情
     *
     * @param pId
     * @return
     */
    Posts selectByPId(@Param("pId") Integer pId);

    Integer selectLikeCountByPid(@Param("pId") Integer pId);

    Integer selectCommentCountById(@Param("pId") Integer pId);

    Posts selectPostsDetailsByPid(@Param("pId") Integer pId, @Param("openid") String openid);

    List<Posts> selectPostsByOpenid(@Param("openid") String openid);

    List<Posts> selectPostsPage(Integer currentPage, Integer pageSize, @Param("openid") String openid);
}




