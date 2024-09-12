package com.zuu.chatroom.chat.controller;

import com.zuu.chatroom.chat.domain.vo.req.*;
import com.zuu.chatroom.chat.domain.vo.resp.AtMemberListResp;
import com.zuu.chatroom.chat.domain.vo.resp.ChatMemberResp;
import com.zuu.chatroom.chat.domain.vo.resp.MemberResp;
import com.zuu.chatroom.chat.service.GroupService;
import com.zuu.chatroom.common.domain.vo.req.IdReqVO;
import com.zuu.chatroom.common.domain.vo.resp.ApiResult;
import com.zuu.chatroom.common.domain.vo.resp.CursorPageBaseResp;
import com.zuu.chatroom.common.domain.vo.resp.IdRespVO;
import com.zuu.chatroom.common.utils.RequestHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zuu
 * @Description
 * @Date 2024/8/6 21:36
 */
@RestController
@Tag(name = "群组相关接口")
@RequestMapping("/room")
public class GroupController {
    @Resource
    GroupService groupService;

    @GetMapping("/public/group")
    @Operation(summary = "群组详情")
    public ApiResult<MemberResp> groupDetail(@Valid IdReqVO request) {
        Long uid = RequestHolder.get().getId();
        return ApiResult.success(groupService.getGroupDetail(uid, request.getId()));
    }
    @GetMapping("/public/group/member/page")
    @Operation(summary = "群成员列表")
    public ApiResult<CursorPageBaseResp<ChatMemberResp>> getMemberPage(@Valid MemberReq memberReq) {
        return ApiResult.success(groupService.getMemberPage(memberReq));
    }

    @GetMapping("/group/member/list")
    @Operation(summary = "房间内的所有群成员列表-@专用")
    public ApiResult<List<AtMemberListResp>> getMemberList(@Valid AtMemberListReq atMemberListReq) {
        return ApiResult.success(groupService.getMemberList(atMemberListReq));
    }

    @DeleteMapping("/group/member")
    @Operation(summary = "移除成员")
    public ApiResult<Void> delMember(@Valid @RequestBody MemberDelReq memberDelReq) {
        Long uid = RequestHolder.get().getId();
        groupService.delMember(uid, memberDelReq);
        return ApiResult.success();
    }

    @DeleteMapping("/group/member/exit")
    @Operation(summary = "退出群聊")
    public ApiResult<Boolean> exitGroup(@Valid @RequestBody MemberExitReq memberExitReq) {
        Long uid = RequestHolder.get().getId();
        groupService.exitGroup(uid, memberExitReq);
        return ApiResult.success();
    }

    @PostMapping("/group")
    @Operation(summary = "新增群组")
    public ApiResult<IdRespVO> addGroup(@Valid @RequestBody GroupAddReq groupAddReq) {
        Long uid = RequestHolder.get().getId();
        Long roomId = groupService.addGroup(uid, groupAddReq);
        return ApiResult.success(IdRespVO.id(roomId));
    }

    @PostMapping("/group/member")
    @Operation(summary = "邀请好友")
    public ApiResult<Void> addMember(@Valid @RequestBody MemberAddReq memberAddReq) {
        Long uid = RequestHolder.get().getId();
        groupService.addMember(uid, memberAddReq);
        return ApiResult.success();
    }

    @PutMapping("/group/admin")
    @Operation(summary = "添加管理员")
    public ApiResult<Boolean> addAdmin(@Valid @RequestBody AdminAddReq adminAddReq) {
        Long uid = RequestHolder.get().getId();
        groupService.addAdmin(uid, adminAddReq);
        return ApiResult.success();
    }

    @DeleteMapping("/group/admin")
    @Operation(summary = "撤销管理员")
    public ApiResult<Boolean> revokeAdmin(@Valid @RequestBody AdminRevokeReq adminRevokeReq) {
        Long uid = RequestHolder.get().getId();
        groupService.revokeAdmin(uid, adminRevokeReq);
        return ApiResult.success();
    }
}
