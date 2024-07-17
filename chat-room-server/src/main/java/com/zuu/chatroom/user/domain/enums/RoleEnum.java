package com.zuu.chatroom.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/17 09:21
 */
@AllArgsConstructor
@Getter
public enum RoleEnum {
    SUPER_ADMIN(1L,"超级管理员"),
    CHAT_MANAGER(2L,"群聊管理员")
    ;
    private final Long roleId;
    private final String desc;
}
