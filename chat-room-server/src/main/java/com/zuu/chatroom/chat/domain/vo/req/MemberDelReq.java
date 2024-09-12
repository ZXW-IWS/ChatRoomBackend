package com.zuu.chatroom.chat.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDelReq {
    @NotNull
    @Schema(title ="会话id")
    private Long roomId;

    @NotNull
    @Schema(title ="被移除的uid")
    private Long uid;
}