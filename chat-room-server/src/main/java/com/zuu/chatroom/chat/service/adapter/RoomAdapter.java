package com.zuu.chatroom.chat.service.adapter;

import com.zuu.chatroom.chat.domain.enums.GroupRoleEnum;
import com.zuu.chatroom.chat.domain.enums.RoomFriendStatusEnum;
import com.zuu.chatroom.chat.domain.po.GroupMember;
import com.zuu.chatroom.chat.domain.po.Room;
import com.zuu.chatroom.chat.domain.po.RoomFriend;
import com.zuu.chatroom.chat.domain.po.RoomGroup;
import com.zuu.chatroom.common.domain.enums.YesOrNoEnum;
import com.zuu.chatroom.user.domain.po.User;

import java.util.List;
import java.util.stream.Collectors;

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

    public static RoomGroup buildGroupRoom(User user, Long roomId, List<User> userList) {
        RoomGroup roomGroup = new RoomGroup();
        StringBuffer sb = new StringBuffer(user.getNickname() + ",");
        sb.append(userList.stream().map(User::getNickname).collect(Collectors.joining(",")));
        roomGroup.setName(sb.toString());
        roomGroup.setAvatar(user.getAvatar());
        roomGroup.setRoomId(roomId);

        return roomGroup;
    }

    public static GroupMember buildGroupMember(Long uid, Long groupId, GroupRoleEnum groupRoleEnum) {
        GroupMember groupMember = new GroupMember();
        groupMember.setGroupId(groupId);
        groupMember.setUid(uid);
        groupMember.setRole(groupRoleEnum.getType());
        return groupMember;
    }
}
