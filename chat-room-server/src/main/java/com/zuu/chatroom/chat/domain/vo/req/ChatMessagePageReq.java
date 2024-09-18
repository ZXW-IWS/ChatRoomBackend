package com.zuu.chatroom.chat.domain.vo.req;

import com.zuu.chatroom.common.domain.vo.req.CursorPageBaseReq;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/27 10:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatMessagePageReq extends CursorPageBaseReq {
    @NotNull
    private Long roomId;
}
