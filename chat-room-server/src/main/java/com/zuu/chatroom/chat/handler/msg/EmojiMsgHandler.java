package com.zuu.chatroom.chat.handler.msg;

import cn.hutool.json.JSONUtil;
import com.zuu.chatroom.chat.domain.dto.EmojisMsgDTO;
import com.zuu.chatroom.chat.domain.dto.FileMsgDTO;
import com.zuu.chatroom.chat.domain.entity.MessageExtra;
import com.zuu.chatroom.chat.domain.enums.MsgTypeEnum;
import com.zuu.chatroom.chat.domain.po.Message;
import com.zuu.chatroom.chat.domain.vo.req.ChatMessageReq;
import com.zuu.chatroom.chat.service.MessageService;
import com.zuu.chatroom.chat.service.adapter.MessageAdapter;
import com.zuu.chatroom.common.utils.ChatUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/22 15:52
 */
@Component
public class EmojiMsgHandler implements MsgHandler{
    @Resource
    MessageService messageService;
    @Override
    public MsgTypeEnum getMsgTypeEnum() {
        return MsgTypeEnum.EMOJI;
    }

    @Override
    public Long checkAndSaveMsg(ChatMessageReq chatMessageReq, Long uid) {
        //校验注解信息
        String bodyJson = JSONUtil.toJsonStr(chatMessageReq.getBody());
        EmojisMsgDTO emojisMsgDTO = JSONUtil.toBean(bodyJson, EmojisMsgDTO.class);
        ChatUtils.allCheckValidateThrow(emojisMsgDTO);
        //保存消息
        Message message = MessageAdapter.buildBaseMessage(chatMessageReq,uid);
        MessageExtra extra = new MessageExtra();
        extra.setEmojisMsgDTO(emojisMsgDTO);
        message.setExtra(extra);

        messageService.save(message);

        return message.getId();
    }

    @Override
    public Object showMsg(Message msg) {
        return msg.getExtra().getEmojisMsgDTO();
    }

    @Override
    public Object showReplyMsg(Message msg) {
        return msg.getExtra().getEmojisMsgDTO();
    }

    @Override
    public String showContactMsg(Message msg) {
        return "[表情消息]";
    }
}
