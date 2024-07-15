package com.zuu.chatroom.user.domain.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/14 16:26
 */
@Data
public class BadgeResp {
    @Schema(title = "徽章id")
    private Long id;

    @Schema(title = "徽章图标")
    private String img;

    @Schema(title = "徽章描述")
    private String describe;

    @Schema(title = "是否拥有 0-否 1-是")
    private Integer obtain;

    @Schema(title = "是否佩戴 0-否 1-是")
    private Integer wearing;
}
