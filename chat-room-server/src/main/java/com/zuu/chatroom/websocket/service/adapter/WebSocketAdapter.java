package com.zuu.chatroom.websocket.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.zuu.chatroom.chat.domain.dto.MsgRecallDto;
import com.zuu.chatroom.chat.domain.enums.GroupMemberChangeEnum;
import com.zuu.chatroom.chat.domain.vo.resp.ChatMessageResp;
import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.websocket.domain.enums.WsBaseRespTypeEnum;
import com.zuu.chatroom.websocket.domain.vo.resp.*;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.stereotype.Component;

/**
 * @Author zuu
 * @Description adapter类用于构建websocket的返回信息
 * @Date 2024/7/6 11:39
 */
@Component
public class WebSocketAdapter {
    public static WsBaseResp buildNewApplyMsgResp(Long uid, Long unReadCount) {
        WsFriendApply wsFriendApply = new WsFriendApply();
        wsFriendApply.setUid(uid);
        wsFriendApply.setUnreadCount(unReadCount);

        WsBaseResp wsBaseResp = new WsBaseResp();
        wsBaseResp.setType(WsBaseRespTypeEnum.APPLY.getType());
        wsBaseResp.setData(wsFriendApply);

        return wsBaseResp;
    }

    public static WsBaseResp buildMsgSendResp(ChatMessageResp msgResp) {
        WsBaseResp wsBaseResp = new WsBaseResp();
        wsBaseResp.setType(WsBaseRespTypeEnum.MESSAGE.getType());
        wsBaseResp.setData(msgResp);

        return wsBaseResp;
    }

    public static WsBaseResp buildInvalidTokenResp() {
        WsBaseResp wsBaseResp = new WsBaseResp();
        wsBaseResp.setType(WsBaseRespTypeEnum.INVALIDATE_TOKEN.getType());
        return wsBaseResp;
    }

    public static WsBaseResp buildScanSuccessResp() {
        WsBaseResp wsBaseResp = new WsBaseResp();
        wsBaseResp.setType(WsBaseRespTypeEnum.LOGIN_SCAN_SUCCESS.getType());
        return wsBaseResp;
    }

    public static WsBaseResp buildQrcodeResp(WxMpQrCodeTicket wxMpQrCodeTicket){
        WsBaseResp wsBaseResp = new WsBaseResp();
        wsBaseResp.setType(WsBaseRespTypeEnum.LOGIN_URL.getType());
        wsBaseResp.setData(new WsQrcodeResp(wxMpQrCodeTicket.getUrl()));
        return wsBaseResp;
    }

    public static WsBaseResp buildLoginSuccessResp(User user, String token, boolean hasPower) {
        WsBaseResp wsBaseResp = new WsBaseResp();
        wsBaseResp.setType(WsBaseRespTypeEnum.LOGIN_SUCCESS.getType());
        WsLoginSuccess wsLoginSuccess = new WsLoginSuccess(user,token,hasPower);
        wsBaseResp.setData(wsLoginSuccess);
        return wsBaseResp;
    }

    public static WsBaseResp buildMsgRecallResp(MsgRecallDto msgRecallDto) {
        WsBaseResp wsBaseResp = new WsBaseResp();

        wsBaseResp.setType(WsBaseRespTypeEnum.RECALL.getType());
        WSMsgRecall wsMsgRecall = new WSMsgRecall();
        BeanUtil.copyProperties(msgRecallDto,wsMsgRecall);
        wsBaseResp.setData(wsMsgRecall);

        return wsBaseResp;
    }

    public static WsBaseResp buildMemberRemovedResp(Long roomId, Long deletedUid) {
        WsBaseResp wsBaseResp = new WsBaseResp();
        wsBaseResp.setType(WsBaseRespTypeEnum.MEMBER_CHANGE.getType());

        WSMemberChange wsMemberChange = new WSMemberChange();
        wsMemberChange.setRoomId(roomId);
        wsMemberChange.setUid(deletedUid);
        wsMemberChange.setChangeType(GroupMemberChangeEnum.EXIT.getType());
        wsBaseResp.setData(wsMemberChange);

        return wsBaseResp;
    }

    public static WsBaseResp buildGroupAddAResp(User invitedUser, Long roomId) {
        WsBaseResp wsBaseResp = new WsBaseResp();
        wsBaseResp.setType(WsBaseRespTypeEnum.MEMBER_CHANGE.getType());

        WSMemberChange wsMemberChange = new WSMemberChange();
        wsMemberChange.setUid(invitedUser.getId());
        wsMemberChange.setActiveStatus(invitedUser.getActiveStatus());
        wsMemberChange.setLastOptTime(invitedUser.getLastLoginTime());
        wsMemberChange.setRoomId(roomId);
        wsMemberChange.setChangeType(GroupMemberChangeEnum.ADD.getType());
        wsBaseResp.setData(wsMemberChange);

        return wsBaseResp;
    }
}
