package com.zuu.chatroom.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/14 17:27
 */
@AllArgsConstructor
@Getter
public enum CommonErrorEnum implements ErrorEnum{
    BUSINESS_EXCEPTION(0,""),
    SYSTEM_ERROR(-1,"系统出小差了，请稍后再试哦～"),
    PARAM_INVALID(-2,"参数检验失败");
    ;
    private final Integer errorCode;
    private final String errorMsg;
}
