package com.zuu.chatroom.chat.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseFileDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(title ="大小（字节）")
    @NotNull
    private Long size;

    @Schema(title ="下载地址")
    @NotBlank
    private String url;
}