package com.zuu.chatroom.chat.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberAddReq {
    @NotNull
    @Schema(title ="房间id")
    private Long roomId;

    @NotNull
    @Size(min = 1, max = 50)
    @Schema(title ="邀请的uid")
    private List<Long> uidList;
}