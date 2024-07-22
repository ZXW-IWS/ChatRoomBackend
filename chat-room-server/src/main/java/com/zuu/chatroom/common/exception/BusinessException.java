package com.zuu.chatroom.common.exception;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/14 18:49
 */
@Data
public class BusinessException extends RuntimeException{

    private Integer code;
    private String message;

    public BusinessException(){
    }
    public BusinessException(String message){
        this.code = CommonErrorEnum.BUSINESS_EXCEPTION.getErrorCode();
        this.message = message;
    }
    public BusinessException(Integer code,String message){
        this.code = code;
        this.message = message;
    }
    public BusinessException(CommonErrorEnum commonErrorEnum){
        this(commonErrorEnum.getErrorCode(),commonErrorEnum.getErrorMsg());
    }
    public BusinessException(BusinessException e){
        this.code = e.getCode();
        this.message = e.getMessage();
    }
}
