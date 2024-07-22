package com.zuu.chatroom.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/18 21:25
 */
@AllArgsConstructor
@Getter
public enum ApplyReadStatusEnum {
    NO_READ(1,"未读"),
    READ(2,"已读")
    ;

    private final Integer status;
    private final String desc;
}
