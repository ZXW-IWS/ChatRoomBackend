package com.zuu.chatroom.websocket.listener;

import com.zuu.chatroom.user.domain.po.UserApply;
import com.zuu.chatroom.user.service.FriendService;
import com.zuu.chatroom.user.service.UserApplyService;
import com.zuu.chatroom.websocket.domain.vo.resp.WsBaseResp;
import com.zuu.chatroom.websocket.service.WebSocketService;
import com.zuu.chatroom.websocket.service.adapter.WebSocketAdapter;
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
 * @Date 2024/7/20 23:22
 */
@Component
@Slf4j
public class ApplyListener {
    @Resource
    private WebSocketService webSocketService;
    @Resource
    private UserApplyService userApplyService;
    @Resource
    private FriendService friendService;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = APPLY_QUEUE_NAME,durable = "true"),
            exchange = @Exchange(name = APPLY_EXCHANGE_NAME),
            key = APPLY_KEY
    ))
    public void applyMsgHandler(Long applyId){
        UserApply userApply = userApplyService.getById(applyId);
        if(Objects.isNull(userApply))
            return;
        log.info("receive apply message,this apply is:[{}]",userApply);
        Long uid = userApply.getUid();
        Long targetId = userApply.getTargetId();
        Long unReadCount = friendService.unread(targetId).getUnReadCount();
        WsBaseResp wsBaseResp = WebSocketAdapter.buildNewApplyMsgResp(uid,unReadCount);
        webSocketService.sendToUser(targetId,wsBaseResp);
    }
}
