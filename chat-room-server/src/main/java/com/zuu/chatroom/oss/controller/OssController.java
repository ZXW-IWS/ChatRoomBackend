package com.zuu.chatroom.oss.controller;

import com.zuu.chatroom.common.domain.vo.resp.ApiResult;
import com.zuu.chatroom.common.utils.RequestHolder;
import com.zuu.chatroom.oss.domain.vo.req.UploadUrlReq;
import com.zuu.chatroom.oss.domain.vo.resp.OssResp;
import com.zuu.chatroom.oss.service.OssService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/29 15:02
 */
@RestController
@RequestMapping("/oss")
@Tag(name = "oss接口")
public class OssController {
    @Resource
    private OssService ossService;

    @GetMapping("/upload/url")
    @Operation(summary = "获取临时上传链接")
    public ApiResult<OssResp> getUploadUrl(@Valid UploadUrlReq req) {
        return ApiResult.success(ossService.getUploadUrl(RequestHolder.get().getId(), req));
    }
}
