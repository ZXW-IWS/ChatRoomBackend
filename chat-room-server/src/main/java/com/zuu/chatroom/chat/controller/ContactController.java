package com.zuu.chatroom.chat.controller;

import com.zuu.chatroom.chat.domain.vo.req.ContactFriendReq;
import com.zuu.chatroom.chat.domain.vo.resp.ChatRoomResp;
import com.zuu.chatroom.chat.service.ContactService;
import com.zuu.chatroom.common.domain.vo.req.CursorPageBaseReq;
import com.zuu.chatroom.common.domain.vo.req.IdReqVO;
import com.zuu.chatroom.common.domain.vo.resp.ApiResult;
import com.zuu.chatroom.common.domain.vo.resp.CursorPageBaseResp;
import com.zuu.chatroom.common.utils.RequestHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author zuu
 * @Description
 * @Date 2024/8/6 14:16
 */
@RestController
@RequestMapping("/chat")
@Tag(name = "会话相关接口")
public class ContactController {
    @Resource
    private ContactService contactService;
    @GetMapping("/public/contact/list")
    @Operation(summary = "会话列表")
    public ApiResult<CursorPageBaseResp<ChatRoomResp>> getRoom(@Valid CursorPageBaseReq request) {
        Long uid = RequestHolder.get().getId();
        return ApiResult.success(contactService.getContactList(uid));
    }

    @GetMapping("/public/contact/detail")
    @Operation(summary = "会话详情(发消息后需要刷新会话详情)")
    public ApiResult<ChatRoomResp> getContactDetail(@Valid IdReqVO request) {
        Long uid = RequestHolder.get().getId();
        return ApiResult.success(contactService.getContactDetail(uid, request.getId()));
    }

    @GetMapping("/public/contact/detail/friend")
    @Operation(summary = "会话详情(给联系人发消息时使用创建会话)")
    public ApiResult<ChatRoomResp> getContactDetailByFriend(@Valid ContactFriendReq request) {
        Long uid = RequestHolder.get().getId();
        return ApiResult.success(contactService.getContactDetailByFriend(uid, request.getUid()));
    }
}
