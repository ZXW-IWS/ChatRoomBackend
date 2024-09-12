package com.zuu.chatroom.chat.handler.msg;

import cn.hutool.json.JSONUtil;
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

import java.util.Optional;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/22 15:50
 */
@Component
public class FileMsgHandler implements MsgHandler{
    @Resource
    MessageService messageService;
    @Override
    public MsgTypeEnum getMsgTypeEnum() {
        return MsgTypeEnum.FILE;
    }

    @Override
    public Long checkAndSaveMsg(ChatMessageReq chatMessageReq, Long uid) {
        //校验注解信息
        String bodyJson = JSONUtil.toJsonStr(chatMessageReq.getBody());
        FileMsgDTO fileMsgDTO = JSONUtil.toBean(bodyJson, FileMsgDTO.class);
        ChatUtils.allCheckValidateThrow(fileMsgDTO);
        //保存消息
        Message message = MessageAdapter.buildBaseMessage(chatMessageReq,uid);
        MessageExtra extra = new MessageExtra();
        extra.setFileMsgDTO(fileMsgDTO);
        message.setExtra(extra);

        messageService.save(message);

        return message.getId();
    }

    @Override
    public Object showMsg(Message msg) {
        MessageExtra extra = Optional.ofNullable(msg.getExtra()).orElse(new MessageExtra());
        return extra.getFileMsgDTO();
    }

    @Override
    public Object showReplyMsg(Message msg) {
        return "文件:" + msg.getExtra().getFileMsgDTO().getFileName();
    }

    @Override
    public String showContactMsg(Message msg) {
        return "文件:" + msg.getExtra().getFileMsgDTO().getFileName();
    }
}
