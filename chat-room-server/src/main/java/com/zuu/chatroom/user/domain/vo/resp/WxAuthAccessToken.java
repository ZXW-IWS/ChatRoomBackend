package com.zuu.chatroom.user.domain.vo.resp;

import lombok.Data;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/4 10:02
 */
@Data
public class WxAuthAccessToken {
    private String accessToken;
    private int expiresIn = -1;
    private String refreshToken;
    private String openid;
    private String scope;
}
