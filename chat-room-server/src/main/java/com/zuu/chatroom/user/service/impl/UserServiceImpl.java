package com.zuu.chatroom.user.service.impl;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuu.chatroom.common.exception.BusinessException;
import com.zuu.chatroom.common.utils.RedisUtils;
import com.zuu.chatroom.user.domain.enums.ItemEnum;
import com.zuu.chatroom.user.domain.enums.ItemTypeEnum;
import com.zuu.chatroom.user.domain.po.ItemPackage;
import com.zuu.chatroom.user.domain.vo.req.ModifyNameReq;
import com.zuu.chatroom.user.domain.vo.resp.UserInfoResp;
import com.zuu.chatroom.user.mapper.UserMapper;
import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.user.service.ItemPackageService;
import com.zuu.chatroom.user.service.UserService;
import com.zuu.chatroom.user.service.adapter.UserAdapter;
import jakarta.annotation.Resource;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static com.zuu.chatroom.common.constant.RedisConstant.USER_TOKEN_KEY;
import static com.zuu.chatroom.common.constant.RedisConstant.USER_TOKEN_TTL;

/**
* @author zuu
* @description 针对表【user(chat-room用户表)】的数据库操作Service实现
* @createDate 2024-07-06 09:57:19
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private ItemPackageService itemPackageService;

    @Override
    @Transactional
    public void register(String openid) {
        User user = new User();
        user.setOpenid(openid);

        //TODO: 封装全局异常处理器后处理失败信息
        boolean saved = this.save(user);
    }


    @Override
    public String login(Long id) {
        String token = IdUtil.simpleUUID();
        String key = USER_TOKEN_KEY + token;
        //token保存到redis
        RedisUtils.set(key,id,USER_TOKEN_TTL, TimeUnit.HOURS);

        return token;
    }

    /**
     * 校验token是不是有效
     * @param token
     * @return
     */
    @Override
    public boolean verify(String token) {
        String key = USER_TOKEN_KEY + token;
        Long id = RedisUtils.get(key, Long.class);

        return !ObjectUtil.isNull(id);
    }

    @Override
    public void renewalTokenIfNecessary(String token) {
        if(!verify(token))
            return;
        String key = USER_TOKEN_KEY + token;
        //刷新token有效事件
        RedisUtils.expire(key,USER_TOKEN_TTL,TimeUnit.HOURS);
    }

    @Override
    public Long getIdByToken(String token) {
        String key = USER_TOKEN_KEY + token;

        return RedisUtils.get(key, Long.class);
    }

    @Override
    @Transactional
    public User fillUserInfo(WxOAuth2UserInfo userInfo) {
        User user = this.getOne(new QueryWrapper<User>().eq("openid", userInfo.getOpenid()));
        if(ObjectUtil.isNull(user)){
            //TODO: 异常
            return null;
        }
        //若用户头像昵称不为空，就不需要更新用户信息
        if(StrUtil.isAllNotBlank(user.getNickname(),user.getAvatar())){
            return user;
        }
        //否则就需要更新
        user.setNickname(userInfo.getNickname());
        user.setAvatar(userInfo.getHeadImgUrl());
        user.setSex(userInfo.getSex());
        boolean updated = this.updateById(user);
        //TODO: 异常
        return user;
    }

    /**
     * 获取用户信息
     * @param id
     * @return
     */
    @Override
    public UserInfoResp getUserInfo(Long id) {
        User user = this.getById(id);
        int modifyNameChance = itemPackageService.getCountByItemId(id, ItemEnum.MODIFY_NAME_CARD.getId());
        return UserAdapter.buildUserInfoResp(user,modifyNameChance);
    }

    /**
     * 修改用户名
     *
     * @param id            用户id
     * @param modifyNameReq
     */
    @Override
    @Transactional
    public void updateUserName(Long id, ModifyNameReq modifyNameReq) {
        //1. 判断用户是否有改名卡
        ItemPackage modifyItem = itemPackageService.getFirstItem(id, ItemEnum.MODIFY_NAME_CARD.getId());
        if(ObjectUtil.isNull(modifyItem))
            throw new BusinessException("您还没有改名卡");
        //2.使用改名卡并修改用户名
        boolean useSuccess = itemPackageService.useItem(modifyItem.getId());
        if(useSuccess){
            //改名
            this.update(new UpdateWrapper<User>()
                    .eq("id",id)
                    .set("nickname",modifyNameReq.getNickname()));
        }
    }
}




