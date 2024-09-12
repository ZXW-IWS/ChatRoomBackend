package com.zuu.chatroom.chat.domain.vo.req;

import com.zuu.chatroom.common.domain.vo.req.CursorPageBaseReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberReq extends CursorPageBaseReq {
    @Schema(title ="房间号")
    private Long roomId = 1L;
}