package com.zuu.chatroom.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zuu.chatroom.common.domain.vo.req.PageBaseReq;
import com.zuu.chatroom.user.domain.po.UserApply;

import java.util.List;

/**
* @author zuu
* @description 针对表【user_apply(用户申请表)】的数据库操作Service
* @createDate 2024-07-18 09:30:26
*/
public interface UserApplyService extends IService<UserApply> {

    Page<UserApply> getApplyList(Long uid, PageBaseReq getApplyListReq);

    void readApples(List<Long> applyIds);

    Long getUnReadCount(Long uid);

    void agree(Long applyId);

    /**
     * 获取好友申请记录
     */
    UserApply getApply(Long uid, Long targetUid);

    Long saveApply(Long uid, Long targetUid, String applyReqMsg);
}
