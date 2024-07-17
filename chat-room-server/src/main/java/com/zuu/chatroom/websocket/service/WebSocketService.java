package com.zuu.chatroom.websocket.service;

import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.websocket.domain.vo.resp.WsBaseResp;
import io.netty.channel.Channel;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/6 11:24
 */
public interface WebSocketService {
    void doOffline(Channel channel);

    void sendQrcodeUrl(Channel channel);

    void connect(Channel channel);

    void scanSuccess(Integer loginCode);

    void scanLoginSuccess(Integer loginCode, Long id);

    void authorizeByToken(Channel channel, String token);

    /**
     * 推送消息给所有在线用户
     * @param wsBaseResp 消息
     * @param skipUid 需要跳过的人
     */
    void sendToAllOnlineUser(WsBaseResp wsBaseResp, Long skipUid);
}
