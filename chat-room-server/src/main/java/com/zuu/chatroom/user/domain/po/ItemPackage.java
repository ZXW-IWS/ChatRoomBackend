package com.zuu.chatroom.user.domain.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户背包表
 * @TableName item_package
 */
@TableName(value ="item_package")
@Data
public class ItemPackage implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 物品id
     */
    private Long itemId;

    /**
     * 是否使用 0-未使用 1-已使用
     */
    private Integer status;

    /**
     * 幂等号
     */
    private String idempotent;

    /**
     * 记录创建时间
     */
    private Date createTime;

    /**
     * 记录更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除 0-未删除 1-已删除
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}