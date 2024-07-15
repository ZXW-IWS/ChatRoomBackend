package com.zuu.chatroom.common.interceptor;

import com.zuu.chatroom.common.exception.HttpErrorEnum;
import com.zuu.chatroom.user.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;
import java.util.Optional;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/14 12:42
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ID_KEY = "id";

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       String token = getToken(request);
       if(Objects.nonNull(token)){
           //用户已登录
           Long id = userService.getIdByToken(token);
           request.setAttribute(ID_KEY,id);
       }else{
           //是否是公共接口
           boolean isPublicUri = isPublicUri(request);
           if(!isPublicUri){
               //返回401未登录
               HttpErrorEnum.NO_LOGIN.sendErrorResp(response);
               return false;
           }
       }

       return true;
    }

    private  boolean isPublicUri(HttpServletRequest request) {
        //用户未登录，判断是否访问公共接口
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/");
        //uri的开头有一个"/"，因此split数组的0位置上有一个空字符串
        return split.length > 2 && "public".equals(split[2]);
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER_AUTHORIZATION);
        return Optional.ofNullable(header)
                .filter(h -> h.startsWith(TOKEN_PREFIX))
                .map(h -> h.replaceFirst(TOKEN_PREFIX, ""))
                .orElse(null);
    }
}
