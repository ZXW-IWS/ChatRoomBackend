package com.zuu.chatroom.common.interceptor;

import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.zuu.chatroom.common.domain.dto.RequestInfo;
import com.zuu.chatroom.common.utils.RequestHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

/**
 * @Author zuu
 * @Description tokenInterceptor之后用于收集本次http请求相关的信息并将其存到threadLocal里
 * @Date 2024/7/14 14:59
 */
@Component
public class RequestInfoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long id = Optional.ofNullable(request.getAttribute(TokenInterceptor.ID_KEY))
                .map(Object::toString)
                .map(Long::parseLong)
                .orElse(null);
        String clientIP = JakartaServletUtil.getClientIP(request);
        RequestInfo requestInfo = new RequestInfo(id,clientIP);
        //将请求信息存入threadLocal
        RequestHolder.set(requestInfo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        RequestHolder.remove();
    }
}
