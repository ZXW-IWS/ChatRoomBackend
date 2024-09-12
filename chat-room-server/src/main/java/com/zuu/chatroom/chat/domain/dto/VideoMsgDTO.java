package com.zuu.chatroom.chat.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
public class VideoMsgDTO extends BaseFileDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(title ="缩略图宽度（像素）")
    @NotNull
    private Integer thumbWidth;

    @Schema(title ="缩略图高度（像素）")
    @NotNull
    private Integer thumbHeight;

    @Schema(title ="缩略图大小（字节）")
    @NotNull
    private Long thumbSize;

    @Schema(title ="缩略图下载地址")
    @NotBlank
    private String thumbUrl;

}