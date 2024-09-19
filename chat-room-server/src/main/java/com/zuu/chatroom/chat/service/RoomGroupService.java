package com.zuu.chatroom.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zuu.chatroom.chat.domain.po.RoomGroup;
import com.zuu.chatroom.user.domain.po.User;

import java.util.List;

/**
* @author zuu
* @description 针对表【room_group(群聊房间表)】的数据库操作Service
* @createDate 2024-07-22 14:15:04
*/
public interface RoomGroupService extends IService<RoomGroup> {

    RoomGroup getByRoomId(Long roomId);

    RoomGroup createGroup(User user, Long roomId, List<User> userList);

    void removeByRoomId(Long roomId);
}
