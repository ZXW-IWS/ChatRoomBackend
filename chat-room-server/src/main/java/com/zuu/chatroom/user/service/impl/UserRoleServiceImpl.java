package com.zuu.chatroom.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuu.chatroom.user.domain.po.UserRole;
import com.zuu.chatroom.user.mapper.UserRoleMapper;
import com.zuu.chatroom.user.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author zuu
* @description 针对表【user_role(用户角色关系表)】的数据库操作Service实现
* @createDate 2024-07-17 09:15:08
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService {

    @Override
    public Set<Long> getUserRoleSet(Long uid) {
        List<UserRole> roleList = this.list(new QueryWrapper<UserRole>().eq("uid", uid));
        return roleList.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
    }
}




