package com.zuu.chatroom.user.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemInfoReq {
    @Schema(title =  "徽章信息入参")
    @Size(max = 50)
    private List<infoReq> reqList;

    @Data
    public static class infoReq {
        @Schema(title =  "徽章id")
        private Long itemId;
        @Schema(title =  "最近一次更新徽章信息时间")
        private Long lastModifyTime;
    }
}