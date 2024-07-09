package com.zuu.chatroom.user.service.impl;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuu.chatroom.user.mapper.UserMapper;
import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.user.service.UserService;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author zuu
* @description 针对表【user(chat-room用户表)】的数据库操作Service实现
* @createDate 2024-07-06 09:57:19
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Override
    @Transactional
    public void register(String openid) {
        User user = new User();
        //TODO: 用户注册时通过redis获取最新的uid
        user.setOpenid(openid);
        user.setUid(1L);
        //user.setUid();
        //user.setNickname(userInfo.getNickname());
        //user.setAvatar(userInfo.getHeadimgurl());
        //user.setOpenid(userInfo.getOpenid());
        //user.setUserType(userType.getType());
        //TODO: 封装全局异常处理器后处理失败信息
        boolean saved = this.save(user);
    }

    @Override
    public String login(Long id) {
        return "123";
    }

    @Override
    @Transactional
    public User fillUserInfo(WxOAuth2UserInfo userInfo) {
        User user = this.getOne(new QueryWrapper<User>().eq("openid", userInfo.getOpenid()));
        if(ObjectUtil.isNull(user)){
            //TODO: 异常
            return user;
        }
        //若用户头像昵称均一致，就不需要更新用户信息
        if(StrUtil.equals(user.getNickname(),userInfo.getNickname()) && StrUtil.equals(user.getAvatar(),userInfo.getHeadImgUrl())){
            return user;
        }
        //否则就需要更新
        user.setNickname(userInfo.getNickname());
        user.setAvatar(userInfo.getHeadImgUrl());
        boolean updated = this.updateById(user);
        //TODO: 异常
        return user;
    }
}




