package com.zuu.chatroom.chat.listener;

import com.zuu.chatroom.chat.domain.dto.MsgRecallDto;
import com.zuu.chatroom.chat.domain.enums.RoomTypeEnum;
import com.zuu.chatroom.chat.domain.po.Message;
import com.zuu.chatroom.chat.domain.po.Room;
import com.zuu.chatroom.chat.domain.po.RoomFriend;
import com.zuu.chatroom.chat.service.GroupMemberService;
import com.zuu.chatroom.chat.service.MessageService;
import com.zuu.chatroom.chat.service.RoomFriendService;
import com.zuu.chatroom.chat.service.RoomService;
import com.zuu.chatroom.common.domain.dto.PushMsgDto;
import com.zuu.chatroom.common.service.MqService;
import com.zuu.chatroom.websocket.service.adapter.WebSocketAdapter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.zuu.chatroom.common.constant.RabbitMqConstant.*;

/**
 * @Author zuu
 * @Description
 * @Date 2024/9/8 21:07
 */
@Component
@Slf4j
public class MsgRecallListener {

    @Resource
    MessageService messageService;
    @Resource
    private RoomService roomService;
    @Resource
    private RoomFriendService roomFriendService;
    @Resource
    private GroupMemberService groupMemberService;
    @Resource
    private MqService mqService;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MESSAGE_RECALL_QUEUE_NAME,durable = "true"),
            exchange = @Exchange(name = MESSAGE_RECALL_EXCHANGE_NAME),
            key = MESSAGE_RECALL_KEY
    ))
    public void rcvMsgRecallHandler(MsgRecallDto msgRecallDto){
        Long msgId = msgRecallDto.getMsgId();
        Long roomId = msgRecallDto.getRoomId();
        Long recallUid = msgRecallDto.getRecallUid();

        log.info("receive message recalled,the message id is:[{}]",msgId);
        Message message = messageService.getById(msgId);
        if(Objects.isNull(message)){
            log.error("receive message recalled,but this message is null.the message id is:[{}]",msgId);
            return;
        }
        Room room = roomService.getById(message.getRoomId());
        //获取房间的用户列表
        List<Long> roomMemberUidList = new ArrayList<>();
        if(Objects.equals(room.getType(), RoomTypeEnum.FRIEND.getType())){
            //私聊
            RoomFriend roomFriend = roomFriendService.geyByRoomId(room.getId());
            roomMemberUidList = Arrays.asList(roomFriend.getUid1(),roomFriend.getUid2());
        }else{
            //群聊
            roomMemberUidList = groupMemberService.getGroupMemberList(room.getId());
        }
        mqService.sendPushMsg(new PushMsgDto(roomMemberUidList, WebSocketAdapter.buildMsgRecallResp(msgRecallDto)));
    }
}
