package com.zuu.chatroom.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.user.domain.vo.req.ModifyNameReq;
import com.zuu.chatroom.user.domain.vo.resp.UserInfoResp;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

/**
* @author zuu
* @description 针对表【user(chat-room用户表)】的数据库操作Service
* @createDate 2024-07-06 09:57:19
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param openid
     */
    void register(String openid);

    /**
     * 生成token
     * @param id
     * @return
     */
    String login(Long id);
    /**
     * 校验token是不是有效
     *
     * @param token
     * @return
     */
    boolean verify(String token);

    /**
     * 刷新token有效期
     *
     * @param token
     */
    void renewalTokenIfNecessary(String token);

    Long getIdByToken(String token);

    /**
     * 用户授权后补全用户信息
     * @param userInfo
     * @return
     */
    User fillUserInfo(WxOAuth2UserInfo userInfo);

    UserInfoResp getUserInfo(Long id);

    void updateUserName(Long id, ModifyNameReq modifyNameReq);
}
