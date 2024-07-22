package com.zuu.chatroom.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemInfoDTO {
    @Schema(title =  "徽章id")
    private Long itemId;
    @Schema(title =  "是否需要刷新")
    private Boolean needRefresh = Boolean.TRUE;
    @Schema(title = "徽章图像")
    private String img;
    @Schema(title = "徽章说明")
    private String describe;

    public static ItemInfoDTO skip(Long itemId) {
        ItemInfoDTO dto = new ItemInfoDTO();
        dto.setItemId(itemId);
        dto.setNeedRefresh(Boolean.FALSE);
        return dto;
    }
}