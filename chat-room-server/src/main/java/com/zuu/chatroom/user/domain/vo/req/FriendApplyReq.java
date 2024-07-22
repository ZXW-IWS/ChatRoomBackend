package com.zuu.chatroom.user.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendApplyReq {

    @NotBlank
    @Schema(title = "申请信息")
    private String msg;

    @NotNull
    @Schema(title = "好友uid")
    private Long targetUid;

}