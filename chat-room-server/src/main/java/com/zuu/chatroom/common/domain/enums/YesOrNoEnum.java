package com.zuu.chatroom.common.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/14 16:36
 */
@AllArgsConstructor
@Getter
public enum YesOrNoEnum {
    YES(1,"是"),
    NO(0,"否");
    ;
    private final Integer status;
    private final String desc;
}
