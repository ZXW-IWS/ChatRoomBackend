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
public enum ApplyStatusEnum {
    WAIT_APPROVE(1,"待审批"),
    APPROVED(2,"已同意")
    ;

    private final Integer status;
    private final String desc;
}
