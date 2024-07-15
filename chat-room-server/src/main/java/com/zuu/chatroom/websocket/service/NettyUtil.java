package com.zuu.chatroom.websocket.service;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/10 11:48
 */
public class NettyUtil {
    public static final AttributeKey<String> TOKEN = AttributeKey.valueOf("token");

    public static <T> void setAttr(Channel channel,AttributeKey<T> key,T value){
        Attribute<T> attr = channel.attr(key);
        attr.set(value);
    }

    public static <T> T get(Channel channel,AttributeKey<T> key){
        Attribute<T> attr = channel.attr(key);
        return attr.get();
    }
}
