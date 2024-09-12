package com.zuu.chatroom.chat.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
public class FileMsgDTO extends BaseFileDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(title ="文件名（带后缀）")
    @NotBlank
    private String fileName;

}