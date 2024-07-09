package com.zuu.chatroom.user.domain.po;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

/**
 * chat-room用户表
 * @TableName user
 */
@Data
public class User implements Serializable {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户账号标识（类似qq号）
     */
    private Long uid;

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
     * 用户在线状态 1-微信 2-测试
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

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        User other = (User) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUid() == null ? other.getUid() == null : this.getUid().equals(other.getUid()))
            && (this.getNickname() == null ? other.getNickname() == null : this.getNickname().equals(other.getNickname()))
            && (this.getAvatar() == null ? other.getAvatar() == null : this.getAvatar().equals(other.getAvatar()))
            && (this.getSex() == null ? other.getSex() == null : this.getSex().equals(other.getSex()))
            && (this.getOpenid() == null ? other.getOpenid() == null : this.getOpenid().equals(other.getOpenid()))
            && (this.getActiveStatus() == null ? other.getActiveStatus() == null : this.getActiveStatus().equals(other.getActiveStatus()))
            && (this.getIpInfo() == null ? other.getIpInfo() == null : this.getIpInfo().equals(other.getIpInfo()))
            && (this.getLastLoginTime() == null ? other.getLastLoginTime() == null : this.getLastLoginTime().equals(other.getLastLoginTime()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUid() == null) ? 0 : getUid().hashCode());
        result = prime * result + ((getNickname() == null) ? 0 : getNickname().hashCode());
        result = prime * result + ((getAvatar() == null) ? 0 : getAvatar().hashCode());
        result = prime * result + ((getSex() == null) ? 0 : getSex().hashCode());
        result = prime * result + ((getOpenid() == null) ? 0 : getOpenid().hashCode());
        result = prime * result + ((getActiveStatus() == null) ? 0 : getActiveStatus().hashCode());
        result = prime * result + ((getIpInfo() == null) ? 0 : getIpInfo().hashCode());
        result = prime * result + ((getLastLoginTime() == null) ? 0 : getLastLoginTime().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", uid=").append(uid);
        sb.append(", nickname=").append(nickname);
        sb.append(", avatar=").append(avatar);
        sb.append(", sex=").append(sex);
        sb.append(", openid=").append(openid);
        sb.append(", activeStatus=").append(activeStatus);
        sb.append(", ipInfo=").append(ipInfo);
        sb.append(", lastLoginTime=").append(lastLoginTime);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}