package com.zuu.chatroom.chat.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author zuu
 * @Description
 * @Date 2024/9/9 17:45
 */
@Getter
@AllArgsConstructor
public enum GroupMemberChangeEnum {
    ADD(1,"加入群聊"),
    EXIT(2,"退出群聊")
    ;
    private final Integer type;
    private final String desc;
}
