package com.zuu.chatroom.websocket.domain.vo.resp;

import com.zuu.chatroom.chat.domain.vo.resp.ChatMessageResp;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WsMessage extends ChatMessageResp {
}