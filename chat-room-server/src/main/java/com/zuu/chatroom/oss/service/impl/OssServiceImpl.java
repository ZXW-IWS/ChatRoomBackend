package com.zuu.chatroom.oss.service.impl;

import com.zuu.chatroom.common.exception.BusinessException;
import com.zuu.chatroom.oss.domain.enums.OssSceneEnum;
import com.zuu.chatroom.oss.domain.vo.req.OssReq;
import com.zuu.chatroom.oss.domain.vo.req.UploadUrlReq;
import com.zuu.chatroom.oss.domain.vo.resp.OssResp;
import com.zuu.chatroom.oss.service.OssService;
import com.zuu.chatroom.oss.template.MinIOTemplate;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/29 15:05
 */
@Service
public class OssServiceImpl implements OssService {
    @Resource
    MinIOTemplate minIOTemplate;
    @Override
    public OssResp getUploadUrl(Long uid, UploadUrlReq req) {
        String fileName = req.getFileName();
        Integer scene = req.getScene();
        OssSceneEnum ossSceneEnum = OssSceneEnum.of(scene);
        if(Objects.isNull(ossSceneEnum)){
            throw new BusinessException("文件上传场景错误!");
        }
        OssReq ossReq = new OssReq();
        ossReq.setFilePath(ossSceneEnum.getPath());
        ossReq.setFileName(fileName);
        ossReq.setUid(uid);

        return minIOTemplate.getPreSignedObjectUrl(ossReq);
    }
}
