package com.zuu.chatroom.chat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zuu.chatroom.chat.domain.enums.MsgStatusEnum;
import com.zuu.chatroom.chat.domain.enums.RoomFriendStatusEnum;
import com.zuu.chatroom.chat.domain.enums.RoomTypeEnum;
import com.zuu.chatroom.chat.domain.po.*;
import com.zuu.chatroom.chat.domain.vo.req.ChatMessagePageReq;
import com.zuu.chatroom.chat.domain.vo.req.ChatMessageReq;
import com.zuu.chatroom.chat.domain.vo.resp.ChatMessageResp;
import com.zuu.chatroom.chat.handler.factory.MsgHandlerFactory;
import com.zuu.chatroom.chat.handler.msg.MsgHandler;
import com.zuu.chatroom.chat.service.*;
import com.zuu.chatroom.chat.service.adapter.MessageAdapter;
import com.zuu.chatroom.common.domain.enums.YesOrNoEnum;
import com.zuu.chatroom.common.domain.vo.resp.CursorPageBaseResp;
import com.zuu.chatroom.common.exception.BusinessException;
import com.zuu.chatroom.common.service.MqService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/22 16:13
 */
@Service
public class ChatServiceImpl implements ChatService {
    @Resource
    private MqService mqService;
    @Resource
    private MessageService messageService;
    @Resource
    private MessageMarkService messageMarkService;
    @Resource
    private RoomService roomService;
    @Resource
    private RoomFriendService roomFriendService;
    @Resource
    private RoomGroupService roomGroupService;
    @Resource
    private GroupMemberService groupMemberService;
    @Resource
    private ContactService contactService;
    @Override
    public Long sendMsg(Long uid, ChatMessageReq chatMessageReq) {
        checkUser(uid,chatMessageReq);
        MsgHandler msgHandler = MsgHandlerFactory.getHandlerNotNull(chatMessageReq.getMsgType());
        //保存消息
        Long msgId = msgHandler.checkAndSaveMsg(chatMessageReq, uid);
        mqService.sendMsg(msgId);
        return msgId;
    }

    private void checkUser(Long uid, ChatMessageReq chatMessageReq) {
        Long roomId = chatMessageReq.getRoomId();
        Room room = roomService.getById(roomId);
        if(Objects.isNull(room))
            throw new BusinessException("该房间不存在！");
        //全员展示房间则跳过检验
        if(room.getHotFlag().equals(YesOrNoEnum.YES.getStatus()))
            return;
        //根据房间类型进行检验
        Integer roomType = room.getType();
        if(roomType.equals(RoomTypeEnum.GROUP.getType())){
            RoomGroup roomGroup = roomGroupService.getByRoomId(roomId);
            GroupMember groupMember = groupMemberService.getMember(uid,roomGroup.getId());
            if(Objects.isNull(groupMember))
                throw new BusinessException("您不在该群聊中!");
        }else if(roomType.equals(RoomTypeEnum.FRIEND.getType())){
            RoomFriend roomFriend = roomFriendService.geyByRoomId(roomId);
            //1. 是否存在
            if(Objects.isNull(roomFriend))
                throw new BusinessException("你们不是好友哦～");
            //2. 房间是否被禁用
            if(roomFriend.getStatus().equals(RoomFriendStatusEnum.BAN.getStatus()))
                throw new BusinessException("对方已将您拉黑");
            //3. 该房间是否属于当前用户
            if(!uid.equals(roomFriend.getUid1()) && !uid.equals(roomFriend.getUid2()))
                throw new BusinessException("对方已将您拉黑");
        }
    }

    @Override
    public ChatMessageResp getMsgResp(Long uid, Long msgId) {
        Message message = messageService.getById(msgId);
        if(Objects.isNull(message))
            return new ChatMessageResp();

        List<MessageMark> messageMarkList = messageMarkService.getMarksByMsgId(msgId);
        return MessageAdapter.buildChatMessageResp(uid,message,messageMarkList);
    }

    private List<ChatMessageResp> getMsgBatchResp(Long uid, List<Message> messageList) {
        if(Objects.isNull(messageList) || messageList.isEmpty()){
            return new ArrayList<>();
        }
        List<MessageMark> messageMarkList = messageMarkService.getMarksByBatchMsgId(messageList);

        return MessageAdapter.buildChatMessageBatchResp(uid,messageList,messageMarkList);
    }

    @Override
    public CursorPageBaseResp<ChatMessageResp> getMsgPage(ChatMessagePageReq chatMessagePageReq, Long uid) {
        Long roomId = chatMessagePageReq.getRoomId();
        Integer pageSize = chatMessagePageReq.getPageSize();
        String cursor = chatMessagePageReq.getCursor();
        //获取会话的最后一条消息id(小黑子只会得到退出之前的)
        Long lastMsgId = contactService.getContactLastMsgId(roomId,uid);
        //游标翻页获取当前页的消息列表
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("room_id",roomId)
                .eq("status", MsgStatusEnum.NORMAL.getStatus())
                .le(Objects.nonNull(lastMsgId),"id",lastMsgId);
        if(StrUtil.isNotBlank(cursor)){
            wrapper.le("id",cursor);
        }
        wrapper.orderByDesc("id");
        Page<Message> page = new Page<>(1,pageSize+1);
        Page<Message> messagePage = messageService.page(page,wrapper);

        String respCursor = Optional.ofNullable(CollectionUtil.getLast(messagePage.getRecords()))
                .map(message -> message.getId().toString())
                .orElse(null);
        Boolean isLast = page.getRecords().size() != pageSize+1;
        CursorPageBaseResp<Message> messageCursorPageBaseResp = new CursorPageBaseResp<>(respCursor, isLast, page.getRecords());
        if(messageCursorPageBaseResp.getList().isEmpty()){
            return CursorPageBaseResp.empty();
        }
        //将消息列表中的消息封装
        return CursorPageBaseResp.init(messageCursorPageBaseResp,getMsgBatchResp(uid,messageCursorPageBaseResp.getList()));
    }

}
