package com.zuu.chatroom.websocket.domain.enums;


import com.zuu.chatroom.websocket.domain.vo.req.WsBaseReq;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/3 15:15
 */
@Getter
public enum WsBaseReqTypeEnum {
    LOGIN(1, "请求登录二维码"),
    HEARTBEAT(2, "心跳包"),
    AUTHORIZE(3, "登录认证"),
    ;
    private final Integer type;
    private final String data;

    private final static Map<Integer, WsBaseReqTypeEnum> cache;
    static {
        //Function.identity() 是 Java 8 引入的一个静态方法，它返回一个总是返回其输入参数的函数。
        //这里返回的就是WsBaseReqTypeEnum对象
        cache = Arrays.stream(WsBaseReqTypeEnum.values()).collect(Collectors.toMap(WsBaseReqTypeEnum::getType, Function.identity()));
    }

    WsBaseReqTypeEnum(Integer type, String data){
        this.type = type;
        this.data  = data;
    }

    public static WsBaseReqTypeEnum of(Integer type){
        return cache.get(type);
    }

    public static WsBaseReqTypeEnum of(WsBaseReq baseReq){
        return of(baseReq.getType());
    }
}
