package com.zuu.chatroom.chat.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author zuu
 * @Description
 * @Date 2024/9/7 20:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecallMsgReq {
    @NotNull
    @Schema(title ="消息id")
    private Long msgId;

    @NotNull
    @Schema(title ="会话id")
    private Long roomId;
}
