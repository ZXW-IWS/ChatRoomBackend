package com.zuu.chatroom.user.service.adapter;

import com.zuu.chatroom.user.domain.po.UserApply;
import com.zuu.chatroom.user.domain.vo.resp.FriendApplyResp;

import java.util.List;
import java.util.Map;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/18 21:31
 */
public class FriendAdapter {

    public static List<FriendApplyResp> buildPageApplyResp(List<UserApply> records) {
        return records.stream().map(userApply -> {
            FriendApplyResp friendApplyResp = new FriendApplyResp();
            friendApplyResp.setApplyId(userApply.getId());
            friendApplyResp.setMsg(userApply.getMsg());
            friendApplyResp.setUid(userApply.getUid());
            friendApplyResp.setStatus(userApply.getStatus());
            friendApplyResp.setType(userApply.getType());
            return friendApplyResp;
        }).toList();
    }

}
