package com.zuu.chatroom.user.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/17 09:41
 */
@Data
public class BlackReq {
    @NotNull
    @Schema(title = "拉黑目标uid")
    private Long uid;
}
