package com.zuu.chatroom.chat.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/22 22:00
 */
@AllArgsConstructor
@Getter
public enum RoomFriendStatusEnum {
    NORMAL(0,"正常"),
    BAN(1,"禁用")
    ;
    private final Integer status;
    private final String desc;
}
