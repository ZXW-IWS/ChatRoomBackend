package com.zuu.chatroom.user.controller;

import com.zuu.chatroom.common.domain.vo.req.IdReqVO;
import com.zuu.chatroom.common.domain.vo.resp.ApiResult;
import com.zuu.chatroom.common.domain.vo.resp.IdRespVO;
import com.zuu.chatroom.common.utils.RequestHolder;
import com.zuu.chatroom.user.domain.vo.req.UserEmojiReq;
import com.zuu.chatroom.user.domain.vo.resp.UserEmojiResp;
import com.zuu.chatroom.user.service.UserEmojiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apiguardian.api.API;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zuu
 * @Description
 * @Date 2024/9/14 11:06
 */
@RestController
@RequestMapping("/user/emoji")
@Tag(name = "用户表情包相关接口")
public class UserEmojiController {
    @Resource
    UserEmojiService userEmojiService;


    @GetMapping("/list")
    @Operation(summary = "表情包列表")
    public ApiResult<List<UserEmojiResp>> getEmojisPage() {
        return ApiResult.success(userEmojiService.emojiList(RequestHolder.get().getId()));
    }

    @PostMapping()
    @Operation(summary = "新增表情包")
    public ApiResult<IdRespVO> insertEmojis(@Valid @RequestBody UserEmojiReq req) {
        Long emojiId = userEmojiService.insertEmoji(req, RequestHolder.get().getId());
        return ApiResult.success(IdRespVO.id(emojiId));
    }

    @DeleteMapping()
    @Operation(summary = "删除表情包")
    public ApiResult<Void> deleteEmojis(@Valid @RequestBody IdReqVO reqVO) {
        userEmojiService.removeEmoji(reqVO.getId(), RequestHolder.get().getId());
        return ApiResult.success();
    }
}
