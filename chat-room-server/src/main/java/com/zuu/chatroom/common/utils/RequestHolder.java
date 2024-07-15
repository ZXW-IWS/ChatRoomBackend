package com.zuu.chatroom.common.utils;

import com.zuu.chatroom.common.domain.dto.RequestInfo;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/14 15:13
 */
public class RequestHolder {
    private static final ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<>();

    public static void set(RequestInfo requestInfo){
        threadLocal.set(requestInfo);
    }

    public static RequestInfo get(){
        return threadLocal.get();
    }

    public static void remove(){
        threadLocal.remove();
    }
}
