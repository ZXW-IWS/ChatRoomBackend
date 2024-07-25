package com.zuu.chatroom.chat.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/22 21:02
 */
@AllArgsConstructor
@Getter
public enum MsgMarkTypeEnum {
    LIKE(1,"点赞"),
    DIS_LIKE(2,"举报")
    ;
    private final Integer type;
    private final String desc;
}
