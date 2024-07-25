package com.zuu.chatroom.common.utils;

import com.zuu.chatroom.common.exception.BusinessException;
import com.zuu.chatroom.common.exception.CommonErrorEnum;
import com.zuu.chatroom.common.exception.ErrorEnum;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Iterator;
import java.util.Set;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/23 12:45
 */
public class ChatUtils {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    /**
     * 注解验证参数(全部校验,抛出异常)
     *
     * @param obj
     */
    public static <T> void allCheckValidateThrow(T obj) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        if (!constraintViolations.isEmpty()) {
            StringBuilder errorMsg = new StringBuilder();
            for (ConstraintViolation<T> violation : constraintViolations) {
                //拼接异常信息
                errorMsg.append(violation.getPropertyPath().toString()).append(":").append(violation.getMessage()).append(",");
            }
            throw new BusinessException(
                    CommonErrorEnum.PARAM_INVALID.getErrorCode(),
                    CommonErrorEnum.PARAM_INVALID.getErrorMsg() + "," + errorMsg.toString().substring(0, errorMsg.length() - 1));
        }
    }
}
