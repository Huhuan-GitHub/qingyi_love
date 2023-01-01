package com.neusoft.qingyi.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName t_tag
 */
@TableName(value ="t_tag")
@Data
public class Tag implements Serializable {
    /**
     * 标签表主键
     */
    @TableId(type = IdType.AUTO)
    private Integer tId;

    /**
     * 标签背景颜色，16进制表示
     */
    private String bgColor;

    /**
     * 标签的文字内容
     */
    private String text;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}