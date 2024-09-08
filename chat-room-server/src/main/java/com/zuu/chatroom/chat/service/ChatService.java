package com.zuu.chatroom.chat.service;

import com.zuu.chatroom.chat.domain.vo.req.ChatMessagePageReq;
import com.zuu.chatroom.chat.domain.vo.req.ChatMessageReq;
import com.zuu.chatroom.chat.domain.vo.req.RecallMsgReq;
import com.zuu.chatroom.chat.domain.vo.resp.ChatMessageResp;
import com.zuu.chatroom.common.domain.vo.resp.CursorPageBaseResp;

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

    /**
     * 分页查询消息列表
     */
    CursorPageBaseResp<ChatMessageResp> getMsgPage(ChatMessagePageReq chatMessagePageReq, Long uid);

    void recallMsg(Long uid, RecallMsgReq recallMsgReq);
}
