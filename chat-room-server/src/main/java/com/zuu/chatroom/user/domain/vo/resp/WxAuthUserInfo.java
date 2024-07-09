package com.zuu.chatroom.user.domain.vo.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/4 09:59
 */
@Data
public class WxAuthUserInfo implements Serializable {
    private String openid;
    private String nickname;
    private Integer sex;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String unionid;
    private String[] privileges;
}
