package com.zuu.chatroom.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author zuu
 * @Description 用户类型
 * @Date 2024/7/7 16:00
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum UserTypeEnum {
    WECHAT_USER(1),
    TEST_USER(2)
    ;
    private Integer type;
}
