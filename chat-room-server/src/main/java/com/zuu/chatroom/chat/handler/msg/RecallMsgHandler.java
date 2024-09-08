package com.zuu.chatroom.chat.handler.msg;

import com.zuu.chatroom.chat.domain.dto.MsgRecallDto;
import com.zuu.chatroom.chat.domain.entity.MessageExtra;
import com.zuu.chatroom.chat.domain.entity.MsgRecall;
import com.zuu.chatroom.chat.domain.enums.MsgTypeEnum;
import com.zuu.chatroom.chat.domain.po.Message;
import com.zuu.chatroom.chat.domain.vo.req.ChatMessageReq;
import com.zuu.chatroom.chat.service.MessageService;
import com.zuu.chatroom.common.service.MqService;
import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.user.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/22 15:50
 */
@Component
public class RecallMsgHandler implements MsgHandler{
    @Resource
    MessageService messageService;
    @Resource
    MqService mqService;
    @Resource
    UserService userService;
    @Override
    public MsgTypeEnum getMsgTypeEnum() {
        return MsgTypeEnum.RECALL;
    }

    @Override
    public Long checkAndSaveMsg(ChatMessageReq chatMessageReq, Long uid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object showMsg(Message msg) {
        MsgRecall recall = msg.getExtra().getRecall();
        User userInfo = userService.getById(recall.getRecallUid());
        if (!Objects.equals(recall.getRecallUid(), msg.getFromUid())) {
            return "管理员\"" + userInfo.getNickname() + "\"撤回了一条成员消息";
        }
        return "\"" + userInfo.getNickname() + "\"撤回了一条消息";
    }

    @Override
    public Object showReplyMsg(Message msg) {
        return "消息已撤回";
    }

    @Override
    public String showContactMsg(Message msg) {
        return "消息已撤回";
    }

    public void recall(Long recallUid, Message message) {
        //设置extra信息（撤回的用户id以及撤回时间）
        MessageExtra messageExtra = Optional.ofNullable(message.getExtra()).orElse(new MessageExtra()) ;
        messageExtra.setRecall(new MsgRecall(recallUid,new Date()));

        Message recallMsg = new Message();
        recallMsg.setId(message.getId());
        recallMsg.setRoomId(message.getRoomId());
        recallMsg.setExtra(messageExtra);
        recallMsg.setType(MsgTypeEnum.RECALL.getType());
        messageService.updateById(recallMsg);

        //TODO:发送消息撤回到mq通知对应群组的用户
        mqService.sendRecallMsg(new MsgRecallDto(recallMsg.getId(),recallMsg.getRoomId(),recallUid));
    }
}
