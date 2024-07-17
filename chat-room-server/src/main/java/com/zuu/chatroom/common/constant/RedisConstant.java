package com.zuu.chatroom.common.constant;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/9 19:34
 */
public class RedisConstant {
    /**
     * 系统key的通用前缀
     */
    public static final String BASE_KEY = "chat-room:";

    /**
     * token相关
     */
    public static final String USER_TOKEN_KEY = BASE_KEY + "token:";
    public static final int USER_TOKEN_TTL_HOURS = 2;

    /**
     * 生成二维码的code
     */
    public static final String USER_QR_CODE_KEY = BASE_KEY + "qrcode:";
    public static final int USER_QR_CODE_TTL_MINUTES = 30;

    /**
     * 徽章列表
     */
    public static final String BADGE_LIST_KEY = BASE_KEY + "badges";
    public static final int BADGE_LIST_TTL_MINUTES = 30;

    /**
     * 黑名单列表
     */
    public static final String BLACK_LIST_KEY = BASE_KEY + "black";
    public static final int BLACK_LIST_TTL_MINUTES = 30;
}
