package com.zuu.chatroom.websocket.service.adapter;

import com.zuu.chatroom.chat.domain.vo.resp.ChatMessageResp;
import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.user.domain.po.UserApply;
import com.zuu.chatroom.websocket.domain.enums.WsBaseRespTypeEnum;
import com.zuu.chatroom.websocket.domain.vo.resp.WsBaseResp;
import com.zuu.chatroom.websocket.domain.vo.resp.WsFriendApply;
import com.zuu.chatroom.websocket.domain.vo.resp.WsLoginSuccess;
import com.zuu.chatroom.websocket.domain.vo.resp.WsQrcodeResp;
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
}
