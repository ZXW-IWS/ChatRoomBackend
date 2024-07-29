package com.zuu.chatroom.websocket.listener;

import com.zuu.chatroom.common.domain.dto.PushMsgDto;
import com.zuu.chatroom.common.domain.enums.PushMsgTypeEnum;
import com.zuu.chatroom.websocket.service.WebSocketService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.zuu.chatroom.common.constant.RabbitMqConstant.*;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/26 12:05
 */
@Component
@Slf4j
public class PushListener {

    @Resource
    private WebSocketService webSocketService;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = WEBSOCKET_PUSH_QUEUE_NAME,durable = "true"),
            exchange = @Exchange(name = WEBSOCKET_PUSH_EXCHANGE_NAME),
            key = WEBSOCKET_PUSH_KEY
    ))
    public void pushMsgHandler(PushMsgDto pushMsgDto){
        log.info("receive push msg dto:[{}]",pushMsgDto);

        if(Objects.equals(pushMsgDto.getPushType(), PushMsgTypeEnum.ALL.getType())){
            webSocketService.sendToAllOnlineUser(pushMsgDto.getWsBaseResp(),null);
        }else if(Objects.equals(pushMsgDto.getPushType(),PushMsgTypeEnum.USER.getType())){
            webSocketService.sendToUserList(pushMsgDto.getUidList(),pushMsgDto.getWsBaseResp());
        }
    }
}
