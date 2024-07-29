package com.zuu.chatroom.chat.handler.msg;

import com.zuu.chatroom.chat.domain.enums.MsgTypeEnum;
import com.zuu.chatroom.chat.domain.po.Message;
import com.zuu.chatroom.chat.domain.vo.req.ChatMessageReq;
import com.zuu.chatroom.chat.handler.factory.MsgHandlerFactory;
import jakarta.annotation.PostConstruct;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/22 15:06
 */
public interface MsgHandler {

    /**
     * init方法向factory中注册对应的handler
     */
    @PostConstruct
    default void init(){
        MsgHandlerFactory.register(this.getMsgTypeEnum(),this);
    }

    /**
     * @see MsgTypeEnum
     * @return 获取当前handler的具体类型、
     */
    MsgTypeEnum getMsgTypeEnum();

    /**
     * @param chatMessageReq 前端发送的消息
     * @param uid   发送消息的用户uid
     * @return  消息的id
     */
    Long checkAndSaveMsg(ChatMessageReq chatMessageReq,Long uid);

    /**
     * 展示消息,向前端返回的消息体
     */
    Object showMsg(Message msg);

    /**
     * 被回复时——展示的消息
     */
    Object showReplyMsg(Message msg);

    /**
     * 会话列表——展示的消息
     */
    String showContactMsg(Message msg);
}
