package com.zuu.chatroom.chat.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/23 13:29
 */
@AllArgsConstructor
@Getter
public enum MsgStatusEnum {
    NORMAL(0,"正常"),
    NOT_NORMAL(1,"删除")
    ;
    private final Integer status;
    private final String desc;
}
