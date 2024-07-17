package com.zuu.chatroom.websocket.listener;

import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.websocket.domain.enums.WsBaseRespTypeEnum;
import com.zuu.chatroom.websocket.domain.vo.resp.WsBaseResp;
import com.zuu.chatroom.websocket.domain.vo.resp.WsBlack;
import com.zuu.chatroom.websocket.service.WebSocketService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.zuu.chatroom.common.constant.RabbitMqConstant.*;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/17 10:12
 */
@Component
@Slf4j
public class BlackUserListener {
    @Resource
    WebSocketService webSocketService;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = BLACK_USER_QUEUE_NAME,durable = "true"),
            exchange = @Exchange(name = BLACK_EXCHANGE_NAME),
            key = BLACK_USER_KEY
    ))
    public void blackUserMsgHandler(User blackUser){
        log.info("receive black user msg,the user is:[{}]",blackUser);
        WsBaseResp wsBaseResp = new WsBaseResp();
        wsBaseResp.setType(WsBaseRespTypeEnum.BLACK.getType());
        WsBlack wsBlack = new WsBlack(blackUser.getId());
        wsBaseResp.setData(wsBlack);
        webSocketService.sendToAllOnlineUser(wsBaseResp,blackUser.getId());
    }
}
