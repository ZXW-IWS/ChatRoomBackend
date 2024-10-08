package com.zuu.chatroom.user.domain.po;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.zuu.chatroom.user.domain.entity.IpInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * chat-room用户表
 * @TableName user
 */
@TableName(value ="user",autoResultMap = true)
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
     * 用户佩戴的徽章id
     */
    private Long itemId;

    /**
     * 用户ip信息
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private IpInfo ipInfo;

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
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public void refreshIp(String ip) {
        if(Objects.isNull(ipInfo))
            ipInfo = new IpInfo();
        ipInfo.refreshIp(ip);
    }
}