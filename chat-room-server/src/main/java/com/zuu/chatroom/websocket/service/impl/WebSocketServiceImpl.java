package com.zuu.chatroom.websocket.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.zuu.chatroom.user.domain.dto.WsChannelUserDto;
import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.user.service.UserService;
import com.zuu.chatroom.websocket.domain.vo.resp.WsBaseResp;
import com.zuu.chatroom.websocket.service.WebSocketService;
import com.zuu.chatroom.websocket.service.adapter.WebSocketAdapter;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/6 11:25
 */
@Service
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {
    @Resource
    private WebSocketAdapter webSocketAdapter;
    @Resource
    private UserService userService;
    @Resource
    @Lazy
    private WxMpService wxMpService;

    /**
     * 所有请求登录的code与channel关系
     */
    public static final ConcurrentHashMap<Integer, Channel> WAIT_LOGIN_MAP = new ConcurrentHashMap<>();

    /**
     * 所有已连接的websocket连接列表和用户之间的对应关系，连接建立时初始化用户为空
     */
    private static final ConcurrentHashMap<Channel, WsChannelUserDto> ONLINE_WS_MAP = new ConcurrentHashMap<>();

    /**
     * 所有在线的用户和对应的socket
     */
    private static final ConcurrentHashMap<Long, CopyOnWriteArrayList<Channel>> ONLINE_UID_MAP = new ConcurrentHashMap<>();

    /**
     * 用户上线时，生成channel与user的对应信息
     * @param channel
     */
    @Override
    public void connect(Channel channel) {
        ONLINE_WS_MAP.put(channel,new WsChannelUserDto());
    }

    /**
     * 用户扫码成功后发送扫码成功消息给前端
     * @param loginCode
     */
    @Override
    public void scanSuccess(Integer loginCode) {
        Channel channel = WAIT_LOGIN_MAP.get(loginCode);
        if(ObjectUtil.isNotNull(channel)){
            sendMsg(channel,webSocketAdapter.buildScanSuccessResp());
        }
    }

    /**
     * 用户扫码登录成功逻辑
     */
    @Override
    public void scanLoginSuccess(Integer loginCode, Long id) {
        //确认连接在该机器
        Channel channel = WAIT_LOGIN_MAP.get(loginCode);
        if (Objects.isNull(channel)) {
            return;
        }
        //移除code
        WAIT_LOGIN_MAP.remove(loginCode);
        //TODO: 完成token的生成逻辑
        String token = userService.login(id);
        User user = userService.getById(id);
        sendMsg(channel,webSocketAdapter.buildLoginSuccessResp(user,token));
    }

    @Override
    public void doOffline(Channel channel) {
        //TODO:用户离线逻辑处理
        ONLINE_WS_MAP.remove(channel);
    }

    @Override
    public void sendQrcodeUrl(Channel channel) {
        //生成与随机二维码对应的code
        Integer code = generateCode(channel);
        //生成二维码url
        WxMpQrCodeTicket wxMpQrCodeTicket;
        try {
            wxMpQrCodeTicket = wxMpService
                    .getQrcodeService().qrCodeCreateTmpTicket(code, (int) Duration.ofHours(1).getSeconds());
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
        //发送给前端
        sendMsg(channel,webSocketAdapter.buildQrcodeResp(wxMpQrCodeTicket));
    }

    private Integer generateCode(Channel channel) {
        int code;
        do{
            code = RandomUtil.randomInt(Integer.MAX_VALUE);
        }while(WAIT_LOGIN_MAP.containsKey(code));

        WAIT_LOGIN_MAP.put(code,channel);
        return code;
    }

    @Override
    public void loginSuccess(User user) {

    }




    /**
     * 用于websocket中向客户端发送消息
     * @param channel
     * @param wsBaseResp
     */
    private void sendMsg(Channel channel, WsBaseResp wsBaseResp){
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(wsBaseResp)));
    }
}
