package com.neusoft.qingyi.mapper;

import com.neusoft.qingyi.pojo.MiniUser;
import com.neusoft.qingyi.pojo.PostsComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 29600
 * @description 针对表【t_posts_comment】的数据库操作Mapper
 * @createDate 2023-01-01 21:34:56
 * @Entity com.neusoft.qingyi.pojo.PostsComment
 */
public interface PostsCommentMapper extends BaseMapper<PostsComment> {
    List<PostsComment> selectCommentByPid(@Param("pId") Integer pId);

    List<PostsComment> selectCommentReply(@Param("cId") Integer cId);

    MiniUser selectCommentReplyParentMiniUser(@Param("cParentId") Integer cParentId);
}




