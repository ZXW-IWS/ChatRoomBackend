package com.zuu.chatroom.chat.domain.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.zuu.chatroom.chat.domain.entity.MessageExtra;
import lombok.Data;

/**
 * 消息表
 * autoResultMap确保extra的正常解析
 * @TableName message
 */
@TableName(value ="message",autoResultMap = true)
@Data
public class Message implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会话表id
     */
    private Long roomId;

    /**
     * 消息发送者uid
     */
    private Long fromUid;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 回复的消息内容
     */
    private Long replyMsgId;

    /**
     * 消息状态 0正常 1删除
     */
    private Integer status;

    /**
     * 与回复的消息间隔多少条
     */
    private Integer gapCount;

    /**
     * 消息类型 1正常文本 2.撤回消息
     */
    private Integer type;

    /**
     * 扩展信息
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private MessageExtra extra;

    /**
     * 逻辑删除（0-未删除 1-删除）
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