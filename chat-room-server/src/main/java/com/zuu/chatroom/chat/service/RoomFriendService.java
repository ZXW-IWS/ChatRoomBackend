package com.zuu.chatroom.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zuu.chatroom.chat.domain.po.RoomFriend;

/**
* @author zuu
* @description 针对表【room_friend(单聊房间表)】的数据库操作Service
* @createDate 2024-07-22 14:15:04
*/
public interface RoomFriendService extends IService<RoomFriend> {

    RoomFriend geyByRoomId(Long roomId);

    RoomFriend getFriendRoom(Long uid, Long friendId);

    RoomFriend getByKey(String roomKey);

    void restoreRoom(Long id);

    RoomFriend createFriendRoom(Long roomId, Long uid, Long friendId);

    void banRoomByKey(String roomKey);
}
