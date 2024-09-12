package com.zuu.chatroom.chat.service;

import com.zuu.chatroom.chat.domain.vo.req.*;
import com.zuu.chatroom.chat.domain.vo.resp.AtMemberListResp;
import com.zuu.chatroom.chat.domain.vo.resp.ChatMemberResp;
import com.zuu.chatroom.chat.domain.vo.resp.MemberResp;
import com.zuu.chatroom.common.domain.vo.resp.CursorPageBaseResp;

import java.util.List;

/**
 * @Author zuu
 * @Description
 * @Date 2024/9/9 11:47
 */
public interface GroupService {
    MemberResp getGroupDetail(Long uid, Long roomId);

    List<AtMemberListResp> getMemberList(AtMemberListReq atMemberListReq);

    void delMember(Long uid, MemberDelReq memberDelReq);

    void exitGroup(Long uid, MemberExitReq memberExitReq);

    Long addGroup(Long uid, GroupAddReq groupAddReq);

    void addMember(Long uid, MemberAddReq memberAddReq);

    void addAdmin(Long uid, AdminAddReq adminAddReq);

    void revokeAdmin(Long uid, AdminRevokeReq adminRevokeReq);

    CursorPageBaseResp<ChatMemberResp> getMemberPage(MemberReq memberReq);
}
