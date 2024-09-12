package com.zuu.chatroom.chat.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author zuu
 * @Description
 * @Date 2024/9/9 12:14
 */
@Getter
@AllArgsConstructor
public enum GroupRoleEnum {
    LEADER(1,"群主"),
    MANAGER(2,"管理员"),
    MEMBER(3,"群成员"),
    REMOVED(4,"已退出群聊")
    ;
    private final Integer type;
    private final String desc;
}
