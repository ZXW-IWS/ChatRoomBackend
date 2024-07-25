package com.zuu.chatroom.chat.handler.msg;

import cn.hutool.json.JSONUtil;
import com.zuu.chatroom.chat.domain.entity.MessageExtra;
import com.zuu.chatroom.chat.domain.enums.MsgTypeEnum;
import com.zuu.chatroom.chat.domain.po.Message;
import com.zuu.chatroom.chat.domain.vo.req.ChatMessageReq;
import com.zuu.chatroom.chat.domain.vo.req.TextMsgReq;
import com.zuu.chatroom.chat.domain.vo.resp.TextMsgResp;
import com.zuu.chatroom.chat.service.MessageService;
import com.zuu.chatroom.chat.service.adapter.MessageAdapter;
import com.zuu.chatroom.common.utils.ChatUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/22 15:23
 */
@Component
public class TextMsgHandler implements MsgHandler {
    @Resource
    private MessageService messageService;
    @Override
    public MsgTypeEnum getMsgTypeEnum() {
        return MsgTypeEnum.TEXT;
    }

    /**
     * @param chatMessageReq 前端发送的消息
     * @param uid   发送消息的用户uid
     * @return
     */
    @Override
    @Transactional
    public Long checkAndSaveMsg(ChatMessageReq chatMessageReq, Long uid) {
        //1.校验注解信息
        String bodyJson = JSONUtil.toJsonStr(chatMessageReq.getBody());
        TextMsgReq textMsgReq = JSONUtil.toBean(bodyJson, TextMsgReq.class);
        ChatUtils.allCheckValidateThrow(textMsgReq);
        //TODO: 校验回复消息以及@列表信息

        //保存消息
        Message message = MessageAdapter.buildBaseMessage(chatMessageReq,uid);
        message.setContent(textMsgReq.getContent());
        //TODO: 设置额外消息信息，若有@、回复信息或url跳转信息设置
        messageService.save(message);
        return message.getId();
    }



    @Override
    public Object showMsg(Message msg) {
        TextMsgResp textMsgResp = new TextMsgResp();
        textMsgResp.setContent(msg.getContent());

        return textMsgResp;
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
