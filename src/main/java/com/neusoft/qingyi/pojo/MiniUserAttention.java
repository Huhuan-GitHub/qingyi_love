package com.neusoft.qingyi.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @TableName t_mini_user_attention
 */
@TableName(value = "t_mini_user_attention")
@Data
public class MiniUserAttention implements Serializable {
    /**
     * 关注表主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 发出关注的小程序用户
     */
    private String attentionOpenid;

    /**
     * 被关注的小程序用户
     */
    private String attentionedOpenid;

    /**
     * 关注的时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8") //从数据库读出日期格式时，进行转换的规则
    private Date attentionTime;

    /**
     * 取消关注的时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8") //从数据库读出日期格式时，进行转换的规则
    private Date cancelAttentionTime;

    /**
     * 是否取消关注 0:未取消 1:已取消
     */
    private Integer isCancelAttention;

    @TableField(exist = false)
    private MiniUser attentionedMiniUser;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}