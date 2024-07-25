package com.zuu.chatroom.chat.handler.factory;

import com.zuu.chatroom.chat.domain.enums.MsgTypeEnum;
import com.zuu.chatroom.chat.handler.msg.MsgHandler;
import com.zuu.chatroom.common.exception.BusinessException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author zuu
 * @Description 存储消息处理的工厂类，便于后续消息处理时获取具体的handler
 * @Date 2024/7/22 15:06
 */
public class MsgHandlerFactory {
    private static final Map<Integer, MsgHandler> MSG_HANDLER_MAP = new HashMap<>();

    public static void register(Integer type, MsgHandler msgHandler){
        MSG_HANDLER_MAP.put(type,msgHandler);
    }
    public static void register(MsgTypeEnum msgTypeEnum, MsgHandler msgHandler){
        MSG_HANDLER_MAP.put(msgTypeEnum.getType(), msgHandler);
    }

    public static MsgHandler getHandlerNotNull(Integer type){
        MsgHandler msgHandler = MSG_HANDLER_MAP.get(type);
        if(Objects.isNull(msgHandler))
            throw new BusinessException("消息格式错误");
        return msgHandler;
    }
}
