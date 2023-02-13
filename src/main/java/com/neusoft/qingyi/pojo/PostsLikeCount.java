package com.neusoft.qingyi.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName t_posts_like_count
 */
@TableName(value ="t_posts_like_count")
@Data
public class PostsLikeCount implements Serializable {
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
     * 点赞数量
     */
    private Integer count;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}