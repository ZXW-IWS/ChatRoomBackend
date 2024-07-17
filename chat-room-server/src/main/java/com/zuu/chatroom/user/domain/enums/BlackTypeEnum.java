package com.zuu.chatroom.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/17 09:52
 */
@AllArgsConstructor
@Getter
public enum BlackTypeEnum {
    IP(1,"ip类型"),
    UID(2,"uid类型")
    ;
    private final Integer type;
    private final String desc;
}
