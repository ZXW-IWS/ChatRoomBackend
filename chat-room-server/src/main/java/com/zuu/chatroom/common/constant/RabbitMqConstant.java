package com.zuu.chatroom.common.constant;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/16 11:25
 */
public class RabbitMqConstant {
    /**
     * 注册成功消息队列
     */
    public static final String REGISTER_EXCHANGE_NAME = "register.direct";
    public static final String REGISTER_KEY = "register";
    public static final String REGISTER_ACQUIRE_QUEUE_NAME = "register.acquire.queue";

    /**
     * 用户登录上线消息队列
     */
    public static final String ONLINE_EXCHANGE_NAME = "online.direct";
    public static final String ONLINE_KEY = "online";
    public static final String ONLINE_QUEUE_NAME = "online.success.queue";

    /**
     * 拉黑用户消息队列
     */
    public static final String BLACK_EXCHANGE_NAME = "black.direct";
    public static final String BLACK_USER_KEY = "black.user";
    public static final String BLACK_USER_QUEUE_NAME = "black.user.queue";

    /**
     * 好友申请队列
     */
    public static final String APPLY_EXCHANGE_NAME = "apply.direct";
    public static final String APPLY_KEY = "apply";
    public static final String APPLY_QUEUE_NAME = "apply.queue";

    /**
     * 消息发送成功消息队列
     */
    public static final String MESSAGE_EXCHANGE_NAME = "message.direct";
    public static final String MESSAGE_KEY = "message";
    public static final String MESSAGE_QUEUE_NAME = "message.queue";

    /**
     * 消息撤回消息队列
     */
    public static final String MESSAGE_RECALL_EXCHANGE_NAME = "message.recall.direct";
    public static final String MESSAGE_RECALL_KEY = "message.recall";
    public static final String MESSAGE_RECALL_QUEUE_NAME = "message.recall.queue";

    /**
     * 向websocket服务发送消息消息队列
     */
    public static final String WEBSOCKET_PUSH_EXCHANGE_NAME = "websocket.push.direct";
    public static final String WEBSOCKET_PUSH_KEY = "websocket.push";
    public static final String WEBSOCKET_PUSH_QUEUE_NAME = "websocket.push.queue";
}
