package com.zuu.chatroom.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuu.chatroom.chat.domain.enums.RoomFriendStatusEnum;
import com.zuu.chatroom.chat.domain.po.RoomFriend;
import com.zuu.chatroom.chat.mapper.RoomFriendMapper;
import com.zuu.chatroom.chat.service.RoomFriendService;
import com.zuu.chatroom.chat.service.adapter.RoomAdapter;
import org.springframework.stereotype.Service;

/**
* @author zuu
* @description 针对表【room_friend(单聊房间表)】的数据库操作Service实现
* @createDate 2024-07-22 14:15:04
*/
@Service
public class RoomFriendServiceImpl extends ServiceImpl<RoomFriendMapper, RoomFriend>
    implements RoomFriendService {

    @Override
    public RoomFriend geyByRoomId(Long roomId) {
        return this.getOne(new QueryWrapper<RoomFriend>().eq("room_id", roomId));
    }

    @Override
    public RoomFriend getFriendRoom(Long uid, Long friendId) {
        Long uid1 = Math.min(uid,friendId);
        Long uid2 = Math.max(uid,friendId);

        return this.getOne(new QueryWrapper<RoomFriend>()
                .eq("uid1",uid1).eq("uid2",uid2));
    }

    @Override
    public RoomFriend getByKey(String roomKey) {
        return this.getOne(new QueryWrapper<RoomFriend>().eq("room_key",roomKey));
    }

    @Override
    public void restoreRoom(Long id) {
        this.update(
                new UpdateWrapper<RoomFriend>()
                        .eq("id",id)
                        .set("status", RoomFriendStatusEnum.NORMAL.getStatus()));
    }

    @Override
    public RoomFriend createFriendRoom(Long roomId, Long uid, Long friendId) {
        RoomFriend roomFriend = RoomAdapter.buildFriendRoom(roomId,uid,friendId);
        this.save(roomFriend);
        return roomFriend;
    }

    @Override
    public void banRoomByKey(String roomKey) {
        this.update(
                new UpdateWrapper<RoomFriend>()
                        .eq("room_key",roomKey)
                        .set("status",RoomFriendStatusEnum.BAN.getStatus()));
    }
}




