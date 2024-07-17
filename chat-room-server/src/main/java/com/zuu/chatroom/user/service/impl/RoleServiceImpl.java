package com.zuu.chatroom.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuu.chatroom.user.domain.enums.RoleEnum;
import com.zuu.chatroom.user.domain.po.Role;
import com.zuu.chatroom.user.mapper.RoleMapper;
import com.zuu.chatroom.user.service.RoleService;
import com.zuu.chatroom.user.service.UserRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
* @author zuu
* @description 针对表【role(角色表)】的数据库操作Service实现
* @createDate 2024-07-17 09:15:08
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService {

    @Resource
    private UserRoleService userRoleService;
    @Override
    public boolean hasPower(Long uid, RoleEnum roleEnum) {
        Set<Long> roleIdSet = userRoleService.getUserRoleSet(uid);
        return isSuperAdmin(roleIdSet) || roleIdSet.contains(roleEnum.getRoleId());
    }

    private boolean isSuperAdmin(Set<Long> roleIdSet) {
        return roleIdSet.contains(RoleEnum.SUPER_ADMIN.getRoleId());
    }
}




