package com.neusoft.qingyi.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @TableName t_posts
 */
@TableName(value = "t_posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Posts implements Serializable {
    /**
     * 帖子表主键
     */
    @TableId
    private Integer pId;

    /**
     * 发帖时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8") //从数据库读出日期格式时，进行转换的规则
    private Date sendTime;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 发帖人openid
     */
    private String openid;

    /**
     * 标签表主键
     */
    private Integer tId;

    /**
     * 是否匿名发布
     */
    private Integer notReveal;
    /**
     * 帖子是否删除
     */
    @TableField
    @TableLogic
    private Integer isDeleted;

    public Posts(Integer pId, Date sendTime, String content, String openid, Integer tId, Integer notReveal) {
        this.pId = pId;
        this.sendTime = sendTime;
        this.content = content;
        this.openid = openid;
        this.tId = tId;
        this.notReveal = notReveal;
    }

    /**
     * 帖子图片列表
     */
    @TableField(exist = false)
    private List<PostsImg> postsImgList;

    /**
     * 帖子标签
     */
    @TableField(exist = false)
    private Tag tag;

    /**
     * 发布帖子的小程序用户
     */
    @TableField(exist = false)
    private MiniUser miniUser;


    @ApiModelProperty(value = "指定用户是否关注改帖子的发布者")
    @TableField(exist = false)
    private MiniUserAttention isAttentionPostsMiniUser;

    /**
     *
     */
    @TableField(exist = false)
    private PostsLike postsLike;

    @ApiModelProperty(value = "当前帖子点赞的人数")
    @TableField(exist = false)
    private Integer currentPostsLikeCount;

    @ApiModelProperty(value = "当前帖子评论的人数")
    @TableField(exist = false)
    private Integer currentPostsCommentCount;

    @ApiModelProperty(value = "当前帖子的评论列表")
    @TableField(exist = false)
    private List<PostsComment> postsCommentList;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}