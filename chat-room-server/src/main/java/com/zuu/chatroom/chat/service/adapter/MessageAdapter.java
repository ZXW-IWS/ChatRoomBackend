package com.zuu.chatroom.chat.service.adapter;

import com.zuu.chatroom.chat.domain.enums.MarkedMsgStatusEnum;
import com.zuu.chatroom.chat.domain.enums.MsgMarkTypeEnum;
import com.zuu.chatroom.chat.domain.enums.MsgStatusEnum;
import com.zuu.chatroom.chat.domain.enums.MsgTypeEnum;
import com.zuu.chatroom.chat.domain.po.Message;
import com.zuu.chatroom.chat.domain.po.MessageMark;
import com.zuu.chatroom.chat.domain.vo.req.ChatMessageReq;
import com.zuu.chatroom.chat.domain.vo.resp.ChatMessageResp;
import com.zuu.chatroom.chat.handler.factory.MsgHandlerFactory;
import com.zuu.chatroom.chat.handler.msg.MsgHandler;
import com.zuu.chatroom.common.domain.enums.YesOrNoEnum;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/22 17:33
 */
public class MessageAdapter {
    public static ChatMessageResp buildChatMessageResp(Long uid, Message message, List<MessageMark> messageMarkList) {
        ChatMessageResp chatMessageResp = new ChatMessageResp();
        ChatMessageResp.UserInfo userInfo = new ChatMessageResp.UserInfo();
        userInfo.setUid(message.getFromUid());
        chatMessageResp.setFromUser(userInfo);

        chatMessageResp.setMessage(buildMessageInfo(uid,message,messageMarkList));
        return chatMessageResp;
    }

    private static ChatMessageResp.Message buildMessageInfo(Long uid, Message message, List<MessageMark> messageMarkList) {
        ChatMessageResp.Message msg = new ChatMessageResp.Message();
        msg.setId(message.getId());
        msg.setRoomId(message.getRoomId());
        msg.setSendTime(message.getCreateTime());
        msg.setType(message.getType());

        MsgHandler msgHandler = MsgHandlerFactory.getHandlerNotNull(message.getType());
        msg.setBody(msgHandler.showMsg(message));

        msg.setMessageMark(buildMessageMarkInfo(uid,message,messageMarkList));

        return msg;
    }

    private static ChatMessageResp.MessageMark buildMessageMarkInfo(Long uid, Message message, List<MessageMark> messageMarkList) {
        //获取messageMark中的点赞和举报的用户id集合
        Map<Integer, List<MessageMark>> markMap = messageMarkList.stream().collect(Collectors.groupingBy(MessageMark::getType));
        Set<Long> likedUidSet = Optional.ofNullable(markMap.get(MsgMarkTypeEnum.LIKE.getType()))
                .map(list -> list.stream().map(MessageMark::getUid).collect(Collectors.toSet()))
                .orElse(Collections.emptySet());

        Set<Long> dislikedUidSet = Optional.ofNullable(markMap.get(MsgMarkTypeEnum.DIS_LIKE.getType()))
                .map(list -> list.stream().map(MessageMark::getUid).collect(Collectors.toSet()))
                .orElse(Collections.emptySet());

        //组装返回信息
        ChatMessageResp.MessageMark messageMark = new ChatMessageResp.MessageMark();
        messageMark.setLikeCount(likedUidSet.size());
        messageMark.setUserLike(
                likedUidSet.contains(uid) ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus()
        );
        messageMark.setDislikeCount(dislikedUidSet.size());
        messageMark.setUserDislike(
                dislikedUidSet.contains(uid) ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus()
        );

        return messageMark;
    }

    public static Message buildBaseMessage(ChatMessageReq chatMessageReq, Long uid) {
        Message message = new Message();
        message.setRoomId(chatMessageReq.getRoomId());
        message.setFromUid(uid);
        message.setStatus(MsgStatusEnum.NORMAL.getStatus());
        message.setType(chatMessageReq.getMsgType());

        return message;
    }
}
