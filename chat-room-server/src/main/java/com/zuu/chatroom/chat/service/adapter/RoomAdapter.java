package com.zuu.chatroom.chat.service.adapter;

import com.zuu.chatroom.chat.domain.enums.RoomFriendStatusEnum;
import com.zuu.chatroom.chat.domain.po.Room;
import com.zuu.chatroom.chat.domain.po.RoomFriend;
import com.zuu.chatroom.common.domain.enums.YesOrNoEnum;

/**
 * @Author zuu
 * @Description
 * @Date 2024/9/7 12:54
 */
public class RoomAdapter {
    public static final String SEPARATOR = "_";
    public static Room buildRoom(Integer roomType) {
        Room room = new Room();
        room.setType(roomType);
        room.setHotFlag(YesOrNoEnum.NO.getStatus());
        return room;
    }

    public static String generateKey(Long uid, Long friendId) {
        long min = Math.min(uid,friendId),max = Math.max(uid,friendId);
        return min + SEPARATOR + max;
    }
    public static RoomFriend buildFriendRoom(Long roomId, Long uid, Long friendId) {
        long min = Math.min(uid,friendId),max = Math.max(uid,friendId);
        RoomFriend roomFriend = new RoomFriend();

        roomFriend.setRoomId(roomId);
        roomFriend.setUid1(min);
        roomFriend.setUid2(max);
        roomFriend.setRoomKey(generateKey(uid,friendId));
        roomFriend.setStatus(RoomFriendStatusEnum.NORMAL.getStatus());

        return roomFriend;
    }
}
