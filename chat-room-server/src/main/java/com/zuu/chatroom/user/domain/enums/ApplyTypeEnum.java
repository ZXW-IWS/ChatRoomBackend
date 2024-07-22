package com.zuu.chatroom.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/18 21:07
 */
@AllArgsConstructor
@Getter
public enum ApplyTypeEnum {
    ADD_FRIEND(1,"添加好友")
    ;

    private final Integer type;
    private final String desc;
}
