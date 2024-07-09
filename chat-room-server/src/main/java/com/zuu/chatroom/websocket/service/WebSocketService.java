package com.zuu.chatroom.websocket.service;

import com.zuu.chatroom.user.domain.po.User;
import io.netty.channel.Channel;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/6 11:24
 */
public interface WebSocketService {
    void doOffline(Channel channel);

    void sendQrcodeUrl(Channel channel);

    void loginSuccess(User user);

    void connect(Channel channel);

    void scanSuccess(Integer loginCode);

    void scanLoginSuccess(Integer loginCode, Long id);
}
