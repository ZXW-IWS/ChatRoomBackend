package com.zuu.chatroom.chat.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 消息标记表
 * @TableName message_mark
 */
@TableName(value ="message_mark")
@Data
public class MessageMark implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 消息表id
     */
    private Long msgId;

    /**
     * 标记人uid
     */
    private Long uid;

    /**
     * 标记类型 1点赞 2举报
     */
    private Integer type;

    /**
     * 消息状态 0正常 1取消
     */
    private Integer status;

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