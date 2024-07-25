package com.zuu.chatroom.chat.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/22 21:42
 */
@AllArgsConstructor
@Getter
public enum RoomTypeEnum {
    GROUP(1,"群聊"),
    FRIEND(2,"单聊")
    ;
    private final Integer type;
    private final String desc;
}
