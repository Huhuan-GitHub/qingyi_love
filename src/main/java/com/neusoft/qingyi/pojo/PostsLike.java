package com.neusoft.qingyi.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @TableName t_posts_like
 */
@TableName(value = "t_posts_like")
@Data
public class PostsLike implements Serializable {
    /**
     * 点赞人openid
     */
    private String openid;

    /**
     * 帖子表主键
     */
    @TableField(value = "p_id")
    private Integer p_id;

    /**
     * 点赞状态（0：取消 1：点赞）
     */
    private Integer status;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}