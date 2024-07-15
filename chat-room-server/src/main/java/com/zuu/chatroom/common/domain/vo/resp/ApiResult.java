package com.zuu.chatroom.common.domain.vo.resp;

import com.zuu.chatroom.common.exception.ErrorEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/14 13:56
 */
@Data
@Schema(title = "通用返回提")
public class ApiResult <T> {
    @Schema(title = "成功标识true or false")
    private Boolean success;
    @Schema(title = "错误码")
    private Integer errCode;
    @Schema(title = "错误消息")
    private String errMsg;
    @Schema(title = "返回对象")
    private T data;

    public static <T> ApiResult<T> success() {
        ApiResult<T> result = new ApiResult<T>();
        result.setData(null);
        result.setSuccess(Boolean.TRUE);
        return result;
    }

    public static <T> ApiResult<T> success(T data) {
        ApiResult<T> result = new ApiResult<T>();
        result.setData(data);
        result.setSuccess(Boolean.TRUE);
        return result;
    }

    public static <T> ApiResult<T> fail(Integer code, String msg) {
        ApiResult<T> result = new ApiResult<T>();
        result.setSuccess(Boolean.FALSE);
        result.setErrCode(code);
        result.setErrMsg(msg);
        return result;
    }

    public static <T> ApiResult<T> fail(ErrorEnum errorEnum) {
        return fail(errorEnum.getErrorCode(),errorEnum.getErrorMsg());
    }

    public boolean isSuccess() {
        return this.success;
    }
}
