package com.zuu.chatroom.user.domain.po;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * chat-room用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户性别 0-未知 1-男 2-女
     */
    private Integer sex;

    /**
     * 平台中的用户标识
     */
    private String openid;

    /**
     * 用户类别 1-微信  2-测试 ...
     */
    private Integer userType;

    /**
     * 用户在线状态 1-在线 2-离线
     */
    private Integer activeStatus;

    /**
     * 用户ip信息
     */
    private String ipInfo;

    /**
     * 用户上次登录时间
     */
    private Date lastLoginTime;

    /**
     * 账号状态 0-正常 1-异常
     */
    private Integer status;

    /**
     * 记录创建时间
     */
    private Date createTime;

    /**
     * 记录最近更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除 0-正常 1-删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}