package com.zuu.chatroom.user.service;

import com.zuu.chatroom.common.domain.vo.req.CursorPageBaseReq;
import com.zuu.chatroom.common.domain.vo.req.PageBaseReq;
import com.zuu.chatroom.common.domain.vo.resp.CursorPageBaseResp;
import com.zuu.chatroom.common.domain.vo.resp.PageBaseResp;
import com.zuu.chatroom.user.domain.vo.req.FriendApplyReq;
import com.zuu.chatroom.user.domain.vo.req.FriendApproveReq;
import com.zuu.chatroom.user.domain.vo.req.FriendCheckReq;
import com.zuu.chatroom.user.domain.vo.req.FriendListReq;
import com.zuu.chatroom.user.domain.vo.resp.FriendApplyResp;
import com.zuu.chatroom.user.domain.vo.resp.FriendCheckResp;
import com.zuu.chatroom.user.domain.vo.resp.FriendResp;
import com.zuu.chatroom.user.domain.vo.resp.FriendUnreadResp;

import java.util.List;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/18 11:01
 */
public interface FriendService {
    List<FriendResp> friendList(Long uid);

    /**
     * 分页获取用户的好友申请列表
     */
    PageBaseResp<FriendApplyResp> pageApplyFriend(Long uid, PageBaseReq getApplyListReq);

    FriendCheckResp check(Long uid, FriendCheckReq friendCheckReq);

    FriendUnreadResp unread(Long uid);

    void applyApprove(Long uid, FriendApproveReq friendApproveReq);

    void deleteFriend(Long uid, Long targetUid);

    void applyFriend(Long uid, FriendApplyReq friendApplyReq);
}
