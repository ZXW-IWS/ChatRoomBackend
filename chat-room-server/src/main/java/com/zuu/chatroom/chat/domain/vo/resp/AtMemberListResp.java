package com.zuu.chatroom.chat.domain.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AtMemberListResp {
    @Schema(title ="uid")
    private Long uid;
    @Schema(title ="用户名称")
    private String name;
    @Schema(title ="头像")
    private String avatar;
}