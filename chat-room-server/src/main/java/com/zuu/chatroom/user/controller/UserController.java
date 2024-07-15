package com.zuu.chatroom.user.controller;

import com.zuu.chatroom.common.domain.dto.RequestInfo;
import com.zuu.chatroom.common.domain.vo.resp.ApiResult;
import com.zuu.chatroom.common.interceptor.TokenInterceptor;
import com.zuu.chatroom.common.utils.RequestHolder;
import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.user.domain.vo.req.ModifyNameReq;
import com.zuu.chatroom.user.domain.vo.resp.UserInfoResp;
import com.zuu.chatroom.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/14 13:53
 */
@RestController
@Tag(name = "用户相关接口")
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;
    @GetMapping("/userInfo")
    @Operation(summary = "获取用户信息")
    public ApiResult<UserInfoResp> getUserInfo(){
        RequestInfo requestInfo = RequestHolder.get();
        return ApiResult.success( userService.getUserInfo(requestInfo.getId()));
    }

    @PutMapping("/name")
    @Operation(summary = "修改用户名")
    public ApiResult<Void> updateUserName(@Valid @RequestBody ModifyNameReq modifyNameReq){
        userService.updateUserName(RequestHolder.get().getId(),modifyNameReq);
        return ApiResult.success();
    }

    @GetMapping("/public/test")
    public String test(){
        int a = 1 / 0;
        return "ok";
    }
}
