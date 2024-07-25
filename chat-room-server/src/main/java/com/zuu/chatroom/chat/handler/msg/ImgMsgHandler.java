package com.zuu.chatroom.chat.handler.msg;

import com.zuu.chatroom.chat.domain.enums.MsgTypeEnum;
import com.zuu.chatroom.chat.domain.po.Message;
import com.zuu.chatroom.chat.domain.vo.req.ChatMessageReq;
import org.springframework.stereotype.Component;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/22 15:49
 */
@Component
public class ImgMsgHandler implements MsgHandler{
    @Override
    public MsgTypeEnum getMsgTypeEnum() {
        return MsgTypeEnum.IMG;
    }

    @Override
    public Long checkAndSaveMsg(ChatMessageReq chatMessageReq, Long uid) {
        return null;
    }

    @Override
    public Object showMsg(Message msg) {
        return null;
    }

    @Override
    public Object showReplyMsg(Message msg) {
        return null;
    }

    @Override
    public String showContactMsg(Message msg) {
        return null;
    }
}
