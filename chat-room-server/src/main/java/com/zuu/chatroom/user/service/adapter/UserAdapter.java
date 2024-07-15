package com.zuu.chatroom.user.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.user.domain.vo.resp.UserInfoResp;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/14 16:40
 */
public class UserAdapter {
    public static UserInfoResp buildUserInfoResp(User user, int modifyNameChance) {
        UserInfoResp userInfoResp = new UserInfoResp();
        BeanUtil.copyProperties(user,userInfoResp);
        userInfoResp.setModifyNameChance(modifyNameChance);
        return userInfoResp;
    }
}
