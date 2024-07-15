package com.zuu.chatroom.common.constant;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/9 19:34
 */
public class RedisConstant {
    public static final String BASE_KEY = "chat-room:";
    public static final String USER_TOKEN_KEY = BASE_KEY + "token:";
    public static final int USER_TOKEN_TTL = 2;

    public static final String USER_QR_CODE_KEY = BASE_KEY + "qrcode:";
    public static final int USER_QR_CODE_TTL = 30;
}
