package com.zuu.chatroom.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuu.chatroom.chat.domain.po.GroupMember;
import com.zuu.chatroom.chat.mapper.GroupMemberMapper;
import com.zuu.chatroom.chat.service.GroupMemberService;
import org.springframework.stereotype.Service;

/**
* @author zuu
* @description 针对表【group_member(群成员表)】的数据库操作Service实现
* @createDate 2024-07-22 14:15:04
*/
@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember>
    implements GroupMemberService {

}




