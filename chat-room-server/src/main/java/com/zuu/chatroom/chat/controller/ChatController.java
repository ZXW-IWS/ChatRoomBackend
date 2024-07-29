package com.zuu.chatroom.chat.controller;

import com.zuu.chatroom.chat.domain.vo.req.ChatMessagePageReq;
import com.zuu.chatroom.chat.domain.vo.req.ChatMessageReq;
import com.zuu.chatroom.chat.domain.vo.resp.ChatMessageResp;
import com.zuu.chatroom.chat.service.ChatService;
import com.zuu.chatroom.common.domain.vo.resp.ApiResult;
import com.zuu.chatroom.common.domain.vo.resp.CursorPageBaseResp;
import com.zuu.chatroom.common.utils.RequestHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/22 15:53
 */
@RestController
@RequestMapping("/chat")
@Tag(name = "消息相关接口信息")
public class ChatController {
    @Resource
    private ChatService chatService;

    @PostMapping("/msg")
    @Operation(summary = "发送消息")
    public ApiResult<ChatMessageResp> sendMsg(@Valid @RequestBody ChatMessageReq chatMessageReq) {
        Long msgId = chatService.sendMsg(RequestHolder.get().getId(),chatMessageReq);
        //返回完整消息格式，方便前端展示
        return ApiResult.success(chatService.getMsgResp(RequestHolder.get().getId(),msgId));
    }

    @GetMapping("/public/msg/page")
    @Operation(summary = "消息列表")
    public ApiResult<CursorPageBaseResp<ChatMessageResp>> getMsgPage(@Valid ChatMessagePageReq chatMessagePageReq) {
        CursorPageBaseResp<ChatMessageResp> msgPage = chatService.getMsgPage(chatMessagePageReq, RequestHolder.get().getId());
        return ApiResult.success(msgPage);
    }


}
