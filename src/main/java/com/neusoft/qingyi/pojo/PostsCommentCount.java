package com.neusoft.qingyi.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName t_posts_comment_count
 */
@TableName(value ="t_posts_comment_count")
@Data
public class PostsCommentCount implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 帖子表主键
     */
    private Integer pId;

    /**
     * 评论数量
     */
    private Integer commentCount;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}