package com.zuu.chatroom.user.domain.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEmojiResp {
    /**
     * id
     */
    @Schema(title = "id")
    private Long id;

    /**
     * 表情地址
     */
    @Schema(title = "表情url")
    private String expressionUrl;

}