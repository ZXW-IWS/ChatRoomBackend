package com.zuu.chatroom.chat.controller;

import com.zuu.chatroom.chat.domain.vo.req.ChatMessageReq;
import com.zuu.chatroom.chat.domain.vo.resp.ChatMessageResp;
import com.zuu.chatroom.chat.service.ChatService;
import com.zuu.chatroom.common.domain.vo.resp.ApiResult;
import com.zuu.chatroom.common.utils.RequestHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
