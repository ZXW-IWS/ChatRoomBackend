package com.zuu.chatroom.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zuu.chatroom.chat.domain.po.Room;
import com.zuu.chatroom.chat.domain.po.RoomFriend;

import java.util.Date;
import java.util.List;

/**
* @author zuu
* @description 针对表【room(房间表)】的数据库操作Service
* @createDate 2024-07-22 14:15:04
*/
public interface RoomService extends IService<Room> {

    /**
     * 更新房间的最后一条消息id和发送时间
     */
    void updateLaseMessageInfo(Long roomId, Long msgId, Date msgSendTime);

    List<Long> getHotRoomIds();

    /**
     * 好友添加成功后发送好友添加成功信息
     * @param uid 用户id
     * @param friendId 好友id
     * @return 好友房间信息
     */
    RoomFriend createFriendRoom(Long uid, Long friendId);

    Room createRoom(Integer roomType);

    void banFriendRoom(Long uid, Long friendId);

    Boolean delGroupIfNeed(Long roomId,Long groupId);
}
