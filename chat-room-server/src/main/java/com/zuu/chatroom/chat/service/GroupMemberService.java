package com.zuu.chatroom.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zuu.chatroom.chat.domain.enums.GroupRoleEnum;
import com.zuu.chatroom.chat.domain.po.GroupMember;

import java.util.List;
import java.util.Map;

/**
* @author zuu
* @description 针对表【group_member(群成员表)】的数据库操作Service
* @createDate 2024-07-22 14:15:04
*/
public interface GroupMemberService extends IService<GroupMember> {

    GroupMember getMember(Long uid, Long groupId);

    List<Long> getGroupMemberList(Long groupId);

    void delMember(Long deletedUid, Long groupId);

    GroupMember getFirstManager(Long groupId);

    GroupMember getFirstMember(Long groupId);


    void changeRole(Long id, GroupRoleEnum groupRoleEnum);


    void removeAllGroupMember(Long groupId);

    void addMember(Long uid, Long groupId, GroupRoleEnum groupRoleEnum);

    void addMemberBatch(List<Long> uidList, Long groupId, GroupRoleEnum groupRoleEnum);

    void changeRoleBatch(Long groupId, List<Long> uidList, GroupRoleEnum groupRoleEnum);

    Map<Long, Integer> getMemberRoleBatch(Long groupId, List<Long> groupMemberList);
}
