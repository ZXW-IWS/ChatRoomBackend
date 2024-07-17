package com.zuu.chatroom.websocket.domain.enums;

import com.zuu.chatroom.websocket.domain.vo.resp.WsBlack;
import com.zuu.chatroom.websocket.domain.vo.resp.WsLoginSuccess;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/3 15:33
 */
@Getter
public enum WsBaseRespTypeEnum {
    LOGIN_URL(1, "登录二维码返回", String.class),
    LOGIN_SCAN_SUCCESS(2, "用户扫描成功等待授权", null),
    LOGIN_SUCCESS(3, "用户登录成功返回用户信息", WsLoginSuccess.class),
    //MESSAGE(4, "新消息", WSMessage.class),
    //ONLINE_OFFLINE_NOTIFY(5, "上下线通知", WSOnlineOfflineNotify.class),
    INVALIDATE_TOKEN(6, "使前端的token失效，意味着前端需要重新登录", null),
    BLACK(7, "拉黑用户", WsBlack.class),
    //MARK(8, "消息标记", WSMsgMark.class),
    //RECALL(9, "消息撤回", WSMsgRecall.class),
    //APPLY(10, "好友申请", WSFriendApply.class),
    //MEMBER_CHANGE(11, "成员变动", WSMemberChange.class),
    ;

    private final Integer type;
    private final String desc;
    private final Class<?> dataClass;

    WsBaseRespTypeEnum(Integer type, String desc, Class<?> dataClass) {
        this.type = type;
        this.desc = desc;
        this.dataClass = dataClass;
    }

    private final static Map<Integer, WsBaseRespTypeEnum> cache;

    static {
        cache = Arrays.stream(WsBaseRespTypeEnum.values()).collect(Collectors.toMap(WsBaseRespTypeEnum::getType, Function.identity()));
    }

    public static WsBaseRespTypeEnum of(Integer type) {
        return cache.get(type);
    }
}
