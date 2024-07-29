package com.zuu.chatroom.common.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/26 12:13
 */
@AllArgsConstructor
@Getter
public enum PushMsgTypeEnum {
    ALL(1,"全员推送"),
    USER(2,"指定用户推送")
    ;
    private final Integer type;
    private final String desc;
}
