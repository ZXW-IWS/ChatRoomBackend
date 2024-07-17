package com.zuu.chatroom.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zuu.chatroom.user.domain.enums.RoleEnum;
import com.zuu.chatroom.user.domain.po.Role;

/**
* @author zuu
* @description 针对表【role(角色表)】的数据库操作Service
* @createDate 2024-07-17 09:15:08
*/
public interface RoleService extends IService<Role> {

    boolean hasPower(Long uid, RoleEnum roleEnum);
}
