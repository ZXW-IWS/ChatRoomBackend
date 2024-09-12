package com.zuu.chatroom.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuu.chatroom.chat.domain.po.RoomGroup;
import com.zuu.chatroom.chat.mapper.RoomGroupMapper;
import com.zuu.chatroom.chat.service.GroupMemberService;
import com.zuu.chatroom.chat.service.MessageService;
import com.zuu.chatroom.chat.service.RoomGroupService;
import com.zuu.chatroom.chat.service.adapter.RoomAdapter;
import com.zuu.chatroom.user.domain.po.User;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author zuu
* @description 针对表【room_group(群聊房间表)】的数据库操作Service实现
* @createDate 2024-07-22 14:15:04
*/
@Service
public class RoomGroupServiceImpl extends ServiceImpl<RoomGroupMapper, RoomGroup>
    implements RoomGroupService {
    @Resource
    MessageService messageService;
    @Resource
    GroupMemberService groupMemberService;
    @Override
    public RoomGroup getByRoomId(Long roomId) {
        return this.getOne(new QueryWrapper<RoomGroup>().eq("room_id", roomId));
    }

    @Override
    public RoomGroup createGroup(User user, Long roomId, List<User> userList) {
        RoomGroup roomGroup = RoomAdapter.buildGroupRoom(user,roomId,userList);
        this.save(roomGroup);
        return roomGroup;
    }


}




