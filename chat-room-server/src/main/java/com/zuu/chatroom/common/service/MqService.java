package com.zuu.chatroom.common.service;

import com.zuu.chatroom.common.domain.dto.PushMsgDto;
import com.zuu.chatroom.user.domain.po.User;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/16 16:48
 */
public interface MqService {
    void sendUserOnlineMsg(User user);

    void sendBlackUserMsg(User blackUser);

    void sendUserRegisterMsg(User user);

    void sendApplyMsg(Long applyId);

    void sendMsg(Long msgId);

    void sendPushMsg(PushMsgDto pushMsgDto);
}
