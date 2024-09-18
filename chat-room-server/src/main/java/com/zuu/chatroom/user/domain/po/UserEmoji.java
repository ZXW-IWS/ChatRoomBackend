package com.zuu.chatroom.user.domain.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户表情包
 * @TableName user_emoji
 */
@TableName(value ="user_emoji")
@Data
public class UserEmoji implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户表ID
     */
    private Long uid;

    /**
     * 表情地址
     */
    private String expressionUrl;

    /**
     * 逻辑删除(0-正常,1-删除)
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}