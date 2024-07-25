package com.zuu.chatroom.common.service.impl;

import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.common.service.MqService;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.zuu.chatroom.common.constant.RabbitMqConstant.*;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/16 16:49
 */
@Service
public class MqServiceImpl implements MqService {
    @Resource
    RabbitTemplate rabbitTemplate;

    /**
     * @see com.zuu.chatroom.user.listener.OnlineListener
     */
    @Override
    public void sendUserOnlineMsg(User user) {
        rabbitTemplate.convertAndSend(ONLINE_EXCHANGE_NAME,ONLINE_KEY,user);
    }


    /**
     * @see com.zuu.chatroom.websocket.listener.BlackUserListener
     */
    @Override
    public void sendBlackUserMsg(User blackUser) {
        rabbitTemplate.convertAndSend(BLACK_EXCHANGE_NAME,BLACK_USER_KEY,blackUser);
    }

    /**
     * @see com.zuu.chatroom.user.listener.RegisterListener
     */
    @Override
    public void sendUserRegisterMsg(User user) {
        rabbitTemplate.convertAndSend(REGISTER_EXCHANGE_NAME,REGISTER_KEY,user);
    }

    /**
     * @see com.zuu.chatroom.websocket.listener.ApplyListener
     */
    @Override
    public void sendApplyMsg(Long applyId) {
        rabbitTemplate.convertAndSend(APPLY_EXCHANGE_NAME,APPLY_KEY,applyId);
    }

    @Override
    public void sendMsg(Long msgId) {

    }
}
