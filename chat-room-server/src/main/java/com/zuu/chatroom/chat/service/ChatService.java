package com.zuu.chatroom.chat.service;

import com.zuu.chatroom.chat.domain.vo.req.ChatMessageReq;
import com.zuu.chatroom.chat.domain.vo.resp.ChatMessageResp;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/22 16:13
 */
public interface ChatService {

    /**
     * 发送消息请求逻辑处理
     */
    Long sendMsg(Long uid, ChatMessageReq chatMessageReq);

    /**
     * sendMsg方法执行完毕之后，用于组装消息的返回信息
     */
    ChatMessageResp getMsgResp(Long uid, Long msgId);
}
