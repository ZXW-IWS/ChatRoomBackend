package com.zuu.chatroom.user.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/14 16:46
 */
@Data
public class WearingBadgeReq {
    @Schema(title = "徽章id")
    @NotNull
    private Long id;
}
