package com.neusoft.qingyi.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_mini_user_friend_ship
 */
@TableName(value ="t_mini_user_friend_ship")
@Data
public class MiniUserFriendShip implements Serializable {
    /**
     * 发起好友申请的小程序用户
     */
    private String userOpenid;

    /**
     * 接受好友申请的小程序用户
     */
    private String friendOpenid;

    /**
     * 好友申请的结果：0：待通过，1：已通过，2：已拒绝，3：已删除
     */
    private Integer status;

    /**
     * 好友申请发起的时间
     */
    private Date createTime;

    /**
     * 通过好友申请的时间
     */
    private Date passTime;

    /**
     * 拒绝好友申请的时间
     */
    private Date refuseTime;

    /**
     * 删除好友的时间
     */
    private Date deleteTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}