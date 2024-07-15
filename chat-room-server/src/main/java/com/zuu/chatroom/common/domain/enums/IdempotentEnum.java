package com.zuu.chatroom.common.domain.enums;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author zuu
 * @Description 幂等类型枚举
 * @Date 2024/7/15 15:51
 */
@AllArgsConstructor
@Getter
public enum IdempotentEnum {
    UID(1,"uid"),
    MSG_ID(2,"消息id")
    ;
    private final Integer type;
    private final String desc;

}
