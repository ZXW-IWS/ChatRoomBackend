package com.zuu.chatroom.user.controller;

import com.zuu.chatroom.common.domain.vo.req.CursorPageBaseReq;
import com.zuu.chatroom.common.domain.vo.req.PageBaseReq;
import com.zuu.chatroom.common.domain.vo.resp.ApiResult;
import com.zuu.chatroom.common.domain.vo.resp.CursorPageBaseResp;
import com.zuu.chatroom.common.domain.vo.resp.PageBaseResp;
import com.zuu.chatroom.common.utils.RequestHolder;
import com.zuu.chatroom.user.domain.vo.req.*;
import com.zuu.chatroom.user.domain.vo.resp.FriendApplyResp;
import com.zuu.chatroom.user.domain.vo.resp.FriendCheckResp;
import com.zuu.chatroom.user.domain.vo.resp.FriendResp;
import com.zuu.chatroom.user.domain.vo.resp.FriendUnreadResp;
import com.zuu.chatroom.user.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/18 09:34
 */
@RestController
@RequestMapping("/user/friend")
@Tag(name = "联系人相关接口")
public class FriendController {
    @Resource
    private FriendService friendService;

    @PostMapping("/apply")
    @Operation(summary = "申请好友")
    public ApiResult<Void> apply(@Valid @RequestBody FriendApplyReq friendApplyReq) {
        Long uid = RequestHolder.get().getId();
        friendService.applyFriend(uid, friendApplyReq);
        return ApiResult.success();
    }
    @GetMapping("/page")
    @Operation(summary = "获取用户联系人列表")
    public ApiResult<List<FriendResp>> friendList(){
        Long uid = RequestHolder.get().getId();
        return ApiResult.success(friendService.friendList(uid));
    }

    @GetMapping("/apply/page")
    @Operation(summary = "好友申请列表")
    public ApiResult<PageBaseResp<FriendApplyResp>> page(@Valid PageBaseReq getApplyListReq) {
        Long uid = RequestHolder.get().getId();
        return ApiResult.success(friendService.pageApplyFriend(uid, getApplyListReq));
    }

    @GetMapping("/check")
    @Operation(summary = "批量判断是否是自己好友")
    public ApiResult<FriendCheckResp> check(@Valid FriendCheckReq friendCheckReq) {
        Long uid = RequestHolder.get().getId();
        return ApiResult.success(friendService.check(uid, friendCheckReq));
    }

    @GetMapping("/apply/unread")
    @Operation(summary = "申请未读数")
    public ApiResult<FriendUnreadResp> unread() {
        Long uid = RequestHolder.get().getId();
        return ApiResult.success(friendService.unread(uid));
    }

    @PutMapping("/apply")
    @Operation(summary = "审批同意好友请求")
    public ApiResult<Void> applyApprove(@Valid @RequestBody FriendApproveReq friendApproveReq) {
        friendService.applyApprove(RequestHolder.get().getId(), friendApproveReq);
        return ApiResult.success();
    }

    @DeleteMapping()
    @Operation(summary = "删除好友")
    public ApiResult<Void> delete(@Valid @RequestBody FriendDeleteReq friendDeleteReq) {
        Long uid = RequestHolder.get().getId();
        friendService.deleteFriend(uid, friendDeleteReq.getTargetUid());
        return ApiResult.success();
    }
}
