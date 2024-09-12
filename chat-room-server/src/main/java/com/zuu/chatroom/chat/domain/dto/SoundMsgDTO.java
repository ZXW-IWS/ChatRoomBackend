package com.zuu.chatroom.chat.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoundMsgDTO extends BaseFileDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(title ="时长（秒）")
    @NotNull
    private Integer second;
}