package com.zuu.chatroom.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zuu.chatroom.chat.domain.po.GroupMember;

import java.util.List;

/**
* @author zuu
* @description 针对表【group_member(群成员表)】的数据库操作Service
* @createDate 2024-07-22 14:15:04
*/
public interface GroupMemberService extends IService<GroupMember> {

    GroupMember getMember(Long uid, Long groupId);

    List<Long> getGroupMemberList(Long groupId);
}
