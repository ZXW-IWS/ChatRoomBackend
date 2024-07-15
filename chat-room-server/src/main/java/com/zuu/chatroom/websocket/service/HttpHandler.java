package com.zuu.chatroom.websocket.service;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.net.url.UrlPath;
import cn.hutool.core.net.url.UrlQuery;
import cn.hutool.core.util.URLUtil;
import com.zuu.chatroom.common.utils.RedisUtils;
import io.netty.channel.*;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolConfig;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.ObjectUtil;

import java.net.URI;
import java.util.Optional;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/10 11:33
 */
public class HttpHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) msg;
            //1.获取token
            UrlBuilder urlBuilder = UrlBuilder.ofHttp(httpRequest.uri());
            String token = Optional.ofNullable(urlBuilder.getQuery())
                    .map(urlQuery -> urlQuery.get("token"))
                    .orElse("")
                    .toString();
            //保存token
            NettyUtil.setAttr(ctx.channel(),NettyUtil.TOKEN,token);
            //去除url中/后的内容，以便于后续建立websocket连接使用
            httpRequest.setUri(urlBuilder.getPath().toString());


            ctx.fireChannelRead(httpRequest);
        }  else {
            ctx.fireChannelRead(msg);
        }

    }
}
