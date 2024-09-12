package com.zuu.chatroom.chat.handler.msg;

import cn.hutool.json.JSONUtil;
import com.zuu.chatroom.chat.domain.dto.SoundMsgDTO;
import com.zuu.chatroom.chat.domain.dto.VideoMsgDTO;
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
 * @Date 2024/7/22 15:51
 */
@Component
public class VideoMsgHandler implements MsgHandler {
    @Resource
    MessageService messageService;
    @Override
    public MsgTypeEnum getMsgTypeEnum() {
        return MsgTypeEnum.VIDEO;
    }

    @Override
    public Long checkAndSaveMsg(ChatMessageReq chatMessageReq, Long uid) {
        //校验注解信息
        String bodyJson = JSONUtil.toJsonStr(chatMessageReq.getBody());
        VideoMsgDTO videoMsgDTO = JSONUtil.toBean(bodyJson, VideoMsgDTO.class);
        ChatUtils.allCheckValidateThrow(videoMsgDTO);
        //保存消息
        Message message = MessageAdapter.buildBaseMessage(chatMessageReq,uid);
        MessageExtra extra = new MessageExtra();
        extra.setVideoMsgDTO(videoMsgDTO);
        message.setExtra(extra);

        messageService.save(message);

        return message.getId();
    }

    @Override
    public Object showMsg(Message msg) {
        MessageExtra extra = Optional.ofNullable(msg.getExtra()).orElse(new MessageExtra());
        return extra.getVideoMsgDTO();
    }

    @Override
    public Object showReplyMsg(Message msg) {
        MessageExtra extra = Optional.ofNullable(msg.getExtra()).orElse(new MessageExtra());
        return extra.getVideoMsgDTO();
    }

    @Override
    public String showContactMsg(Message msg) {
        return "[视频]";
    }
}
