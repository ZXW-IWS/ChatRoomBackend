package com.zuu.chatroom.websocket;


import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.zuu.chatroom.websocket.domain.enums.WsBaseReqTypeEnum;
import com.zuu.chatroom.websocket.domain.vo.req.WsBaseReq;
import com.zuu.chatroom.websocket.service.WebSocketService;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author zuu
 * @Description ws连接建立之后，handler用于处理该连接下的消息接收和发送
 */

@Slf4j
@Sharable
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private WebSocketService webSocketService;

    /**
     * ws连接建立时触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.debug("连接建立成功:{}",ctx.channel().id().asLongText());
        //初始化service
        this.webSocketService = SpringUtil.getBean(WebSocketService.class);
        webSocketService.connect(ctx.channel());
    }

    /**
     * 服务器端收到消息处理方法
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.debug("{}收到消息: {}",ctx.channel().id().asLongText(),msg.text());
        WsBaseReq baseReq = JSONUtil.toBean(msg.text(), WsBaseReq.class);
        WsBaseReqTypeEnum wsBaseReqTypeEnum = WsBaseReqTypeEnum.of(baseReq);
        switch (wsBaseReqTypeEnum) {
            case LOGIN:
                //返回登录二维码url
                webSocketService.sendQrcodeUrl(ctx.channel());
                log.debug("发送登录二维码");
                break;
            case AUTHORIZE:
                webSocketService.authorizeByToken(ctx.channel(),baseReq.getData());
                break;
            case HEARTBEAT:
                break;
            default:
                log.info("收到未知类型数据");
        }
    }

    /**
     * ws连接关闭时触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.debug("连接关闭:{}",ctx.channel().id().asLongText());
        userOffLine(ctx);
    }

    /**
     * 心跳检测方法
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            // 读空闲
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.debug("未检测到心跳，连接关闭:{}",ctx.channel().id().asLongText());
                // 关闭用户的连接
                userOffLine(ctx);
            }
        }else if(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            webSocketService.connect(ctx.channel());
            //握手请求
            String token = NettyUtil.get(ctx.channel(), NettyUtil.TOKEN);
            if(StrUtil.isNotBlank(token))
                webSocketService.authorizeByToken(ctx.channel(), token);
        }
        super.userEventTriggered(ctx, evt);
    }


    /**
     * 用户离线处理
     * @param ctx
     */
    private void userOffLine(ChannelHandlerContext ctx) {
        webSocketService.doOffline(ctx.channel());
        ctx.channel().close();
    }

    /**
     * 异常出现时触发
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("异常发生: ",cause);
        ctx.close();
    }



}
