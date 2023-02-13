package com.neusoft.qingyi.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @TableName t_mini_user_chat_message
 */
@TableName(value = "t_mini_user_chat_message")
@Data
public class MiniUserChatMessage implements Serializable {
    /**
     * 消息表主键
     */
    @TableId(type = IdType.AUTO)
    private Integer mId;

    /**
     * 该条消息的发送者
     */
    private String sendOpenid;

    /**
     * 该条消息的接收者
     */
    private String receiveOpenid;

    /**
     * 该条消息的类型 0:文本类型 1:图片类型
     */
    private Integer messageType;

    /**
     * 该条消息的内容，如果是图片，则是图片消息外键
     */
    private String messageContent;

    /**
     * 消息的发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8") //从数据库读出日期格式时，进行转换的规则
    private Date sendTime;

    /**
     * 消息是否删除 0：未删除 1：已删除
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private MiniUser receiveMiniUser;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}