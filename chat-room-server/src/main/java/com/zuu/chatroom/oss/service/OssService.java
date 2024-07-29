package com.zuu.chatroom.oss.service;

import com.zuu.chatroom.oss.domain.vo.req.UploadUrlReq;
import com.zuu.chatroom.oss.domain.vo.resp.OssResp;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/29 15:05
 */
public interface OssService {
    OssResp getUploadUrl(Long uid, UploadUrlReq req);
}
