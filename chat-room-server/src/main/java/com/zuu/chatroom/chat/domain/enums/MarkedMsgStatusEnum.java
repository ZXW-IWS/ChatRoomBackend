package com.zuu.chatroom.chat.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author zuu
 * @Description 消息标记表中每条标记所标记的消息状态
 * @Date 2024/7/22 17:29
 */
@AllArgsConstructor
@Getter
public enum MarkedMsgStatusEnum {
    NORMAL(0,"正常"),
    NOT_NORMAL(1,"不正常")
    ;
    private final Integer status;
    private final String desc;
}
