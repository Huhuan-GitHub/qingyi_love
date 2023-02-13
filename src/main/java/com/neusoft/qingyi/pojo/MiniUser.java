package com.neusoft.qingyi.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @TableName t_mini_user
 */
@TableName(value = "t_mini_user")
@Data
public class MiniUser implements Serializable {
    /**
     * 小程序用户主键
     */
    @TableId(type = IdType.AUTO)
    private Integer miniId;

    /**
     * 小程序用户唯一标识符
     */
    private String openid;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 小程序用户名
     */
    private String username;

    /**
     * 头像地址（腾讯云）
     */
    private String avatar;

    /**
     * 加入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    @ApiModelProperty("小程序用户背景图")
    private String backgroundImage;

    @ApiModelProperty("小程序用户发的帖子")
    @TableField(exist = false)
    private List<Posts> postsList;

    @ApiModelProperty("小程序用户帖子被点赞的次数")
    @TableField(exist = false)
    private Integer likedPostsCount;

    @ApiModelProperty("小程序用户帖子被评论的次数")
    @TableField(exist = false)
    private Integer countedCount;

    @ApiModelProperty("小程序用户关注列表")
    @TableField(exist = false)
    private List<MiniUser> attentionMiniUserList;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}