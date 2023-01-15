package com.neusoft.qingyi.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName t_posts_img
 */
@TableName(value ="t_posts_img")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostsImg implements Serializable {
    /**
     * 帖子表主键
     */
    private Integer pId;

    /**
     * 帖子图片的链接
     */
    private String imgUrl;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}