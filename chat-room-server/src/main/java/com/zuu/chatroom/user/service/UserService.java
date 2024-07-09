package com.zuu.chatroom.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zuu.chatroom.user.domain.po.User;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

/**
* @author zuu
* @description 针对表【user(chat-room用户表)】的数据库操作Service
* @createDate 2024-07-06 09:57:19
*/
public interface UserService extends IService<User> {

    void register(String openid);

    String login(Long id);


    User fillUserInfo(WxOAuth2UserInfo userInfo);
}
