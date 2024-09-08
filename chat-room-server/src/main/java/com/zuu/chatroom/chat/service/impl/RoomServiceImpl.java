package com.zuu.chatroom.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuu.chatroom.chat.domain.enums.RoomFriendStatusEnum;
import com.zuu.chatroom.chat.domain.enums.RoomTypeEnum;
import com.zuu.chatroom.chat.domain.po.Room;
import com.zuu.chatroom.chat.domain.po.RoomFriend;
import com.zuu.chatroom.chat.mapper.RoomMapper;
import com.zuu.chatroom.chat.service.RoomFriendService;
import com.zuu.chatroom.chat.service.RoomGroupService;
import com.zuu.chatroom.chat.service.RoomService;
import com.zuu.chatroom.chat.service.adapter.RoomAdapter;
import com.zuu.chatroom.common.domain.enums.YesOrNoEnum;
import com.zuu.chatroom.common.exception.BusinessException;
import com.zuu.chatroom.user.service.adapter.FriendAdapter;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
* @author zuu
* @description 针对表【room(房间表)】的数据库操作Service实现
* @createDate 2024-07-22 14:15:04
*/
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room>
    implements RoomService {

    @Resource
    RoomFriendService roomFriendService;
    @Resource
    RoomGroupService roomGroupService;
    @Resource
    @Lazy
    RoomService roomService;

    @Override
    @Transactional
    public void updateLaseMessageInfo(Long roomId, Long msgId, Date msgSendTime) {
        this.update(
                new UpdateWrapper<Room>()
                        .eq("id",roomId)
                        .set("last_msg_id",msgId)
                        .set("active_time",msgSendTime));
    }

    @Override
    public List<Long> getHotRoomIds() {
        return this.list(
                new QueryWrapper<Room>()
                        .eq("hot_flag", YesOrNoEnum.YES.getStatus()))
                .stream().map(Room::getId).toList();
    }

    /**
     * @param uid 用户id
     * @param friendId 好友id
     */
    @Override
    @Transactional
    public RoomFriend createFriendRoom(Long uid, Long friendId) {
        if(Objects.isNull(uid) || Objects.isNull(friendId)){
            throw new BusinessException("房间创建失败");
        }
        //获取房间的唯一key，用于判断房间是否曾经存在
        String roomKey = RoomAdapter.generateKey(uid,friendId);

        RoomFriend roomFriend = roomFriendService.getByKey(roomKey);
        if(Objects.nonNull(roomFriend)){
            //房间已存在，恢复房间即可
            this.restoreRoom(roomFriend);
        }else{
            //创建新房间
            Room room = roomService.createRoom(RoomTypeEnum.FRIEND.getType());
            roomFriend = roomFriendService.createFriendRoom(room.getId(),uid,friendId);
        }
        return roomFriend;
    }

    @Override
    @Transactional
    public Room createRoom(Integer roomType) {
        Room room = RoomAdapter.buildRoom(roomType);
        this.save(room);
        return room;
    }

    @Override
    @Transactional
    public void banFriendRoom(Long uid, Long friendId) {
        if(Objects.isNull(uid) || Objects.isNull(friendId)){
            throw new BusinessException("禁用房间失败!");
        }
        String key = RoomAdapter.generateKey(uid,friendId);
        roomFriendService.banRoomByKey(key);
    }

    private void restoreRoom(RoomFriend roomFriend) {
        if(Objects.equals(roomFriend.getStatus(), RoomFriendStatusEnum.BAN.getStatus())){
            roomFriendService.restoreRoom(roomFriend.getId());
        }
    }
}




