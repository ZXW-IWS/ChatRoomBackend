package com.zuu.chatroom.chat.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageReq {
    @NotNull
    @Schema(title = "房间id")
    private Long roomId;

    @Schema(title = "消息类型")
    @NotNull
    private Integer msgType;

    /**
     *
     */
    @Schema(title = "消息内容，类型不同传值不同")
    @NotNull
    private Object body;

}