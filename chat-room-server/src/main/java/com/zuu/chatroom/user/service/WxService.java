package com.zuu.chatroom.user.service;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zuu.chatroom.user.domain.enums.UserTypeEnum;
import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.user.domain.vo.resp.WxAuthAccessToken;
import com.zuu.chatroom.user.domain.vo.resp.WxAuthUserInfo;
import com.zuu.chatroom.websocket.service.WebSocketService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @Author zuu
 * @Description 负责处理与微信api的具体交互
 * @Date 2024/7/4 08:29
 */
@Service
@Slf4j
public class WxService {
/*    @Resource
    private WxLoginProperties wxLoginProperties;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private UserService userService;
    @Resource
    private WebSocketService webSocketService;

    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
    private static final String GET_USER_URL = "https://api.weixin.qq.com/sns/userinfo";

    public void wechatLogin(String code, String state) {

        WxAuthAccessToken wxAuthAccessToken = getWxAuthAccessToken(code);
        log.debug("access_token为:{}",wxAuthAccessToken);

        WxAuthUserInfo userInfo = getWxAuthUserInfo(wxAuthAccessToken);
        log.debug("用户信息为:{}",userInfo);

        //TODO 用户扫码后登录/注册逻辑
        //1.判断用户是否存在
        User user = userService.getOne(new QueryWrapper<User>().eq("openid", userInfo.getOpenid()));
        //2.若用户不存在，则需要先进行用户的注册
        if(user == null)
            user = userService.register(userInfo, UserTypeEnum.WECHAT_USER);
        //3.执行用户登录逻辑
        webSocketService.loginSuccess(user);

    }

    private WxAuthUserInfo getWxAuthUserInfo(WxAuthAccessToken wxAuthAccessToken) {
        String uri = UriComponentsBuilder.fromHttpUrl(GET_USER_URL)
                .queryParam("access_token", wxAuthAccessToken.getAccessToken())
                .queryParam("openid", wxAuthAccessToken.getOpenid())
                .queryParam("lang", "zh_CN")
                .toUriString();
        String response = restTemplate.getForObject(uri, String.class);
        WxAuthUserInfo userInfo = JSONUtil.toBean(response, WxAuthUserInfo.class);
        //TODO 判空逻辑
        return userInfo;
    }

    private WxAuthAccessToken getWxAuthAccessToken(String code) {
        String uri = UriComponentsBuilder.fromHttpUrl(ACCESS_TOKEN_URL)
                .queryParam("appid", wxLoginProperties.getAppId())
                .queryParam("secret", wxLoginProperties.getAppSecret())
                .queryParam("code", code)
                .queryParam("grant_type", "authorization_code")
                .toUriString();
        // 发起请求并获取响应
        String response = restTemplate.getForObject(uri, String.class);
        WxAuthAccessToken wxAuthAccessToken = JSONUtil.toBean(response, WxAuthAccessToken.class);
        //TODO 判空逻辑
        return wxAuthAccessToken;
    }*/
}
