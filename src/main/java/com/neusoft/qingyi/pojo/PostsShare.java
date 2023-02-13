package com.neusoft.qingyi.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName t_posts_share
 */
@TableName(value ="t_posts_share")
@Data
public class PostsShare implements Serializable {
    /**
     * 分享人openid
     */
    private String openid;

    /**
     * 帖子表主键
     */
    private Integer pId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}