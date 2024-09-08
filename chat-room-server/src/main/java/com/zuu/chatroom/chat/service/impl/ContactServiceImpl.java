package com.zuu.chatroom.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuu.chatroom.chat.domain.enums.RoomTypeEnum;
import com.zuu.chatroom.chat.domain.po.*;
import com.zuu.chatroom.chat.domain.vo.resp.ChatRoomResp;
import com.zuu.chatroom.chat.handler.factory.MsgHandlerFactory;
import com.zuu.chatroom.chat.handler.msg.MsgHandler;
import com.zuu.chatroom.chat.mapper.ContactMapper;
import com.zuu.chatroom.chat.service.*;
import com.zuu.chatroom.common.domain.vo.resp.CursorPageBaseResp;
import com.zuu.chatroom.common.exception.BusinessException;
import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.user.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author zuu
* @description 针对表【contact(会话列表)】的数据库操作Service实现
* @createDate 2024-07-22 14:15:04
*/
@Service
public class ContactServiceImpl extends ServiceImpl<ContactMapper, Contact>
    implements ContactService {

    @Resource
    private RoomService roomService;
    @Resource
    private RoomFriendService roomFriendService;
    @Resource
    private RoomGroupService roomGroupService;
    @Resource
    private UserService userService;
    @Resource
    private MessageService messageService;
    @Override
    @Transactional
    public void refreshOrCreateActiveTime(Long roomId, List<Long> roomMemberUidList, Long msgId, Date msgSendTime) {
        this.baseMapper.refreshOrCreateActiveTime(roomId,roomMemberUidList,msgId,msgSendTime);
    }

    @Override
    public Long getContactLastMsgId(Long roomId, Long uid) {
        return Optional.ofNullable(this.getOne(new QueryWrapper<Contact>()
                .eq("uid", uid)
                .eq("room_id", roomId)))
                .map(Contact::getLastMsgId)
                .orElse(-1L);
    }

    @Override
    public CursorPageBaseResp<ChatRoomResp> getContactList(Long uid) {
        List<Long> roomIds = new ArrayList<>();
        if(Objects.isNull(uid)){
            //用户未登录
            roomIds = roomService.getHotRoomIds();
        }else{
            //用户已登录
            //1. 获取用户会话列表
            Set<Long> userRoomIds = this.list(new QueryWrapper<Contact>().eq("uid", uid)).stream().map(Contact::getRoomId).collect(Collectors.toSet());
            //2. 添加用户会话列表中没有的全员会话
            List<Long> hotRoomIds = roomService.getHotRoomIds();
            userRoomIds.addAll(hotRoomIds);

            roomIds = userRoomIds.stream().toList();
        }

        List<ChatRoomResp> chatRoomResps = buildContact(roomIds, uid);
        return CursorPageBaseResp.init(chatRoomResps);
    }

    @Override
    public ChatRoomResp getContactDetail(Long uid, long roomId) {
        Room room = roomService.getById(roomId);
        if(Objects.isNull(room)){
            throw new BusinessException("房间号错误！");
        }
        return buildContact(List.of(roomId),uid).get(0);
    }

    @Override
    public ChatRoomResp getContactDetailByFriend(Long uid, Long friendId) {
        RoomFriend roomFriend = roomFriendService.getFriendRoom(uid,friendId);
        if(Objects.isNull(roomFriend))
            throw new BusinessException("你们不是好友哦～");

        return buildContact(List.of(roomFriend.getRoomId()),uid).get(0);
    }

    private List<ChatRoomResp> buildContact(List<Long> roomIds, Long uid) {
        return roomIds.stream().map(roomId -> {
            Room room = roomService.getById(roomId);

            ChatRoomResp chatRoomResp = new ChatRoomResp();
            chatRoomResp.setRoomId(roomId);
            chatRoomResp.setType(room.getType());
            chatRoomResp.setHot_Flag(room.getHotFlag());
            chatRoomResp.setActiveTime(room.getActiveTime());
            //会话的头像及名称
            if (room.getType().equals(RoomTypeEnum.FRIEND.getType())) {
                RoomFriend roomFriend = roomFriendService.geyByRoomId(roomId);
                Long friendId = roomFriend.getUid1().equals(uid) ? roomFriend.getUid2() : roomFriend.getUid1();
                User friend = userService.getById(friendId);
                chatRoomResp.setName(friend.getNickname());
                chatRoomResp.setAvatar(friend.getAvatar());
            } else {
                RoomGroup roomGroup = roomGroupService.getByRoomId(roomId);
                chatRoomResp.setName(roomGroup.getName());
                chatRoomResp.setAvatar(roomGroup.getAvatar());
            }
            //会话的消息
            Message message = messageService.getById(room.getLastMsgId());
            if (Objects.nonNull(message)) {
                MsgHandler msgHandler = MsgHandlerFactory.getHandlerNotNull(message.getType());
                User fromUser = userService.getById(message.getFromUid());
                chatRoomResp.setText(fromUser.getNickname() + ":" + msgHandler.showContactMsg(message));
            }
            //会话的未读消息数
            Contact contact = this.getOne(new QueryWrapper<Contact>().eq("uid", uid).eq("room_id", roomId));
            if(Objects.nonNull(contact)){
                chatRoomResp.setUnreadCount(messageService.getUnreadCount(roomId, contact.getLastMsgId()));
            }else{
                chatRoomResp.setUnreadCount(0);
            }
            return chatRoomResp;
        }).toList();
    }
}




