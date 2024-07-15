package com.zuu.chatroom.common.exception;

import cn.hutool.http.ContentType;
import com.zuu.chatroom.common.domain.vo.resp.ApiResult;
import com.zuu.chatroom.common.utils.JsonUtils;
import jakarta.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/14 14:07
 */
@Getter
@AllArgsConstructor
public enum HttpErrorEnum implements ErrorEnum{
    NO_LOGIN(401,"用户未登录")
    ;
    private final Integer errorCode;
    private final String errorMsg;


    public void sendErrorResp(HttpServletResponse httpResponse) throws IOException {
        httpResponse.setStatus(this.getErrorCode());
        httpResponse.setContentType(ContentType.JSON.toString(StandardCharsets.UTF_8));
        httpResponse.getWriter().write(JsonUtils.toStr(ApiResult.fail(this)));
    }

}
