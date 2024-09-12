package com.zuu.chatroom.chat.service.adapter;

import com.zuu.chatroom.chat.domain.po.RoomGroup;
import com.zuu.chatroom.chat.domain.vo.resp.MemberResp;

/**
 * @Author zuu
 * @Description
 * @Date 2024/9/9 12:56
 */
public class GroupAdapter {
    public static MemberResp buildGroupDetail(RoomGroup roomGroup, Long onlineCount, Integer groupRole) {
        MemberResp memberResp = new MemberResp();

        memberResp.setRoomId(roomGroup.getRoomId());
        memberResp.setGroupName(roomGroup.getName());
        memberResp.setAvatar(roomGroup.getAvatar());
        memberResp.setOnlineNum(onlineCount);
        memberResp.setRole(groupRole);

        return memberResp;
    }
}
