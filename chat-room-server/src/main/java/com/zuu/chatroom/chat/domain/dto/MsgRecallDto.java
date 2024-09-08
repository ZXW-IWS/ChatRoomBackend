package com.zuu.chatroom.chat.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author zuu
 * @Description
 * @Date 2024/9/8 21:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgRecallDto {
    private Long msgId;
    private Long roomId;
    //撤回的用户
    private Long recallUid;
}
