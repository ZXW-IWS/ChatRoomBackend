package com.zuu.chatroom.common.exception;

import com.zuu.chatroom.common.domain.vo.resp.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/14 17:17
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult<?> methodArgumentNotValidException(MethodArgumentNotValidException e){
        StringBuffer errorMsgBuffer = new StringBuffer();
        e.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> errorMsgBuffer.append(fieldError.getField())
                        .append(fieldError.getDefaultMessage())
                        .append(","));
        String errorMsg = errorMsgBuffer.substring(0,errorMsgBuffer.length()-1);
        log.error("methodArgumentNotValidException occurred! reason is:[{}]",errorMsg);
        return ApiResult.fail(CommonErrorEnum.PARAM_INVALID.getErrorCode(), errorMsg);
    }

    @ExceptionHandler(Throwable.class)
    public ApiResult<?> throwable(Throwable e){
        log.error("System error occurred! reason is:[{}]",e.getMessage(),e);
        return ApiResult.fail(CommonErrorEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(BusinessException.class)
    public ApiResult<?> businessException(BusinessException e){
        log.info("Business exception occurred! reason is:[{}]",e.getMessage());
        return ApiResult.fail(e.getCode(),e.getMessage());
    }

}
