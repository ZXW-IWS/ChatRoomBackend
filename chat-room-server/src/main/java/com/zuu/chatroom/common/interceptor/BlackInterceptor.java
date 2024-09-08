package com.zuu.chatroom.common.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.zuu.chatroom.common.domain.dto.RequestInfo;
import com.zuu.chatroom.common.exception.HttpErrorEnum;
import com.zuu.chatroom.common.utils.RequestHolder;
import com.zuu.chatroom.user.domain.enums.BlackTypeEnum;
import com.zuu.chatroom.user.service.BlackService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @Author zuu
 * @Description tokenInterceptor之后用于收集本次http请求相关的信息并将其存到threadLocal里
 * @Date 2024/7/14 14:59
 */
@Component
public class BlackInterceptor implements HandlerInterceptor {
    @Resource
    BlackService blackService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<Integer, Set<String>> blackMap = blackService.getBlackMap();
        RequestInfo requestInfo = RequestHolder.get();
        //检查用户ip是否在黑名单内
        if(inBlackList(requestInfo.getClientIp(),blackMap.get(BlackTypeEnum.IP.getType()))){
            //返回401未登录
            HttpErrorEnum.NO_LOGIN.sendErrorResp(response);
            return false;
        }
        //检查用户id是否在黑名单内
        if(Objects.nonNull(requestInfo.getId()) && inBlackList(requestInfo.getId().toString(),blackMap.get(BlackTypeEnum.UID.getType()))){
            //返回401未登录
            HttpErrorEnum.NO_LOGIN.sendErrorResp(response);
            return false;
        }
        return true;
    }

    private boolean inBlackList(String target, Set<String> blackSet) {
        if(StrUtil.isBlank(target) || Objects.isNull(blackSet) || blackSet.isEmpty()){
            return false;
        }
        return blackSet.contains(target);
    }

}
