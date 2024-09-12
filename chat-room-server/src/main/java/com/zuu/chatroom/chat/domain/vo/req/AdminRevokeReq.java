package com.zuu.chatroom.chat.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class AdminRevokeReq {
    @NotNull
    @Schema(title ="房间号")
    private Long roomId;

    @NotNull
    @Size(min = 1, max = 3)
    @Schema(title ="需要撤销管理的列表")
    private List<Long> uidList;
}