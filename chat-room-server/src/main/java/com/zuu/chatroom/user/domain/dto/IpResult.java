package com.zuu.chatroom.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/16 17:47
 */
@Data
public class IpResult<T> implements Serializable {
    @Schema(title = "错误码")
    private Integer code;
    @Schema(title = "错误消息")
    private String msg;
    @Schema(title = "返回对象")
    private T data;

    public boolean isSuccess() {
        return Objects.nonNull(this.code) && this.code == 0;
    }
}
