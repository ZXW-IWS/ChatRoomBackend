package com.zuu.chatroom.websocket.domain.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WsFriendApply {
    @Schema(title = "申请人")
    private Long uid;
    @Schema(title = "申请未读数")
    private Long unreadCount;
}