package com.zuu.chatroom.user.controller;

import com.zuu.chatroom.common.domain.dto.RequestInfo;
import com.zuu.chatroom.common.domain.vo.resp.ApiResult;
import com.zuu.chatroom.common.interceptor.TokenInterceptor;
import com.zuu.chatroom.common.utils.RequestHolder;
import com.zuu.chatroom.user.domain.dto.ItemInfoDTO;
import com.zuu.chatroom.user.domain.dto.SummeryInfoDTO;
import com.zuu.chatroom.user.domain.enums.RoleEnum;
import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.user.domain.vo.req.*;
import com.zuu.chatroom.user.domain.vo.resp.BadgeResp;
import com.zuu.chatroom.user.domain.vo.resp.UserInfoResp;
import com.zuu.chatroom.user.service.RoleService;
import com.zuu.chatroom.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Resource
    RoleService roleService;
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

    @GetMapping("/badges")
    @Operation(summary = "用户徽章列表获取")
    public ApiResult<List<BadgeResp>> getBadgeList(){
        return ApiResult.success(userService.getBadgeList(RequestHolder.get().getId()));
    }

    @PutMapping("/badge")
    @Operation(summary = "用户徽章佩戴徽章")
    public ApiResult<Void> wearBadge(@Valid @RequestBody WearingBadgeReq wearingBadgeReq){
        userService.wearBadge(RequestHolder.get().getId(),wearingBadgeReq.getId());
        return ApiResult.success();
    }
    @PutMapping("/black")
    @Operation(summary = "拉黑用户")
    public ApiResult<Void> black(@Valid @RequestBody BlackReq req) {
        Long uid = RequestHolder.get().getId();

        userService.black(uid,req);
        return ApiResult.success();
    }

    @PostMapping("/public/summary/userInfo/batch")
    @Operation(summary = "(懒加载)用户聚合信息-返回的代表需要刷新的")
    public ApiResult<List<SummeryInfoDTO>> getSummeryUserInfo(@Valid @RequestBody SummeryInfoReq req) {
        return ApiResult.success(userService.getSummeryUserInfo(req));
    }

    @PostMapping("/public/badges/batch")
    @Operation(summary = "(懒加载)徽章聚合信息-返回的代表需要刷新的")
    public ApiResult<List<ItemInfoDTO>> getItemInfo(@Valid @RequestBody ItemInfoReq req) {
        return ApiResult.success(userService.getItemInfo(req));
    }
    @GetMapping("/public/test")
    public String test(){
        int a = 1 / 0;
        return "ok";
    }
}
