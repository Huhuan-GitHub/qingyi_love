package com.neusoft.qingyi.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @TableName t_posts_comment
 */
@TableName(value = "t_posts_comment")
@Data
public class PostsComment implements Serializable {
    /**
     * 评论表主键
     */
    @TableId(type = IdType.AUTO)
    @JsonProperty("cId")
    private Integer cId;

    /**
     * 评论人openid
     */
    @JsonProperty("openid")
    private String openid;

    /**
     * 帖子表主键
     */
    @JsonProperty("pId")
    private Integer pId;

    /**
     * 评论内容
     */
    @JsonProperty("comment")
    private String comment;

    /**
     * 评论日期
     */
    @JsonProperty("commentDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date commentDate;

    /**
     * 指向父评论的id，如果不是对评论的回复，那么该值为null
     */
    @JsonProperty("cParentId")
    private Integer cParentId;

    @ApiModelProperty("帖子评论回复列表")
    @TableField(exist = false)
    private List<PostsComment> postsCommentList;

    @ApiModelProperty("回复的小程序用户")
    @TableField(exist = false)
    private MiniUser replyMiniUser;

    @ApiModelProperty("发出评论的小程序用户")
    @TableField(exist = false)
    private MiniUser commentMiniUser;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}