package com.zuu.chatroom.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zuu.chatroom.user.domain.po.UserRole;

import java.util.Set;

/**
* @author zuu
* @description 针对表【user_role(用户角色关系表)】的数据库操作Service
* @createDate 2024-07-17 09:15:08
*/
public interface UserRoleService extends IService<UserRole> {

    Set<Long> getUserRoleSet(Long uid);
}
