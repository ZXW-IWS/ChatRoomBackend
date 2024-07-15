package com.zuu.chatroom.wx.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zuu.chatroom.common.utils.RedisUtils;
import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.user.service.UserService;
import com.zuu.chatroom.websocket.service.WebSocketService;
import com.zuu.chatroom.wx.builder.TextBuilder;
import com.zuu.chatroom.common.constant.WxConstant;
import com.zuu.chatroom.wx.service.WxMsgService;
import jakarta.annotation.Resource;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import static com.zuu.chatroom.common.constant.RedisConstant.USER_QR_CODE_KEY;
import static com.zuu.chatroom.common.constant.RedisConstant.USER_QR_CODE_TTL_MINUTES;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/7 21:31
 */
@Service
public class WxMsgServiceImpl implements WxMsgService {
    @Resource
    private UserService userService;
    @Resource
    private WebSocketService webSocketService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Value(value = "${chat-room.wx.callback}")
    private String redirectUrl;

    /**
     *
     * @param wxMpService
     * @param wxMpXmlMessage 微信推送过来的消息，xml格式. https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html
     * @return
     */
    @Override
    public WxMpXmlOutMessage scan(WxMpService wxMpService, WxMpXmlMessage wxMpXmlMessage) {
        String openid = wxMpXmlMessage.getFromUser();
        Integer loginCode = Integer.parseInt(getEventKey(wxMpXmlMessage));
        //1. 用户扫描二维码之后，向前端发送用户扫码成功的消息。
        webSocketService.scanSuccess(loginCode);
        //2. 若用户已经注册过，那么就执行用户的登录环节并返回
        User user = userService.getOne(new QueryWrapper<User>().eq("openid", openid));
        //2.1 判断用户是否注册过，只需要user不为空且头像或昵称有空值就说明未注册
        if(ObjectUtil.isNotNull(user) && StrUtil.isAllNotBlank(user.getAvatar(),user.getNickname())){
            webSocketService.scanLoginSuccess(loginCode,user.getId());
            //订阅事件可以直接调用scan，返回null则说明用户之前注册过了
            return null;
        }
        //3. 若用户未注册过，那么就还需要进行用户的注册（暂时先用openId进行注册）
        if(ObjectUtil.isNull(user))
            userService.register(openid);

        //4. 发送用户授权的消息，再用户授权之后再补全用户信息
        //将openid与code对应信息存到redis中，后续授权后才能获取到code信息，通知前端登录成功
        String key = USER_QR_CODE_KEY + openid;
        RedisUtils.set(key,loginCode, USER_QR_CODE_TTL_MINUTES, TimeUnit.MINUTES);
        String authorizationUrl = String.format(WxConstant.AUTH_URL, wxMpService.getWxMpConfigStorage().getAppId(), URLEncoder.encode(redirectUrl));
        WxMpXmlOutMessage.TEXT().build();
        return new TextBuilder().build("请点击链接授权：<a href=\"" + authorizationUrl + "\">登录</a>", wxMpXmlMessage, wxMpService);
    }

    /**
     * 用户授权成功
     * @param userInfo
     */
    @Override
    public void authorize(WxOAuth2UserInfo userInfo) {
        User user = userService.fillUserInfo(userInfo);
        String key = USER_QR_CODE_KEY + userInfo.getOpenid();
        Integer code = RedisUtils.get(key,Integer.class);
        webSocketService.scanLoginSuccess(code,user.getId());
    }

    /**
     * 获取生成二维码时传入的code
     */
    private String getEventKey(WxMpXmlMessage wxMpXmlMessage) {
        //扫码关注的渠道事件有前缀，需要去除
        return wxMpXmlMessage.getEventKey().replace("qrscene_", "");
    }
}
