package com.zuu.chatroom.user.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SummeryInfoReq {
    @Schema(title =  "用户信息入参")
    @Size(max = 50)
    private List<infoReq> reqList;

    @Data
    public static class infoReq {
        @Schema(title =  "uid")
        private Long uid;
        @Schema(title =  "最近一次更新用户信息时间")
        private Long lastModifyTime;
    }
}