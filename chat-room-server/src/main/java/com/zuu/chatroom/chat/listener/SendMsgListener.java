package com.zuu.chatroom.chat.listener;

import com.zuu.chatroom.chat.domain.enums.RoomTypeEnum;
import com.zuu.chatroom.chat.domain.po.Message;
import com.zuu.chatroom.chat.domain.po.Room;
import com.zuu.chatroom.chat.domain.po.RoomFriend;
import com.zuu.chatroom.chat.domain.vo.resp.ChatMessageResp;
import com.zuu.chatroom.chat.service.*;
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
 * @Date 2024/7/26 11:56
 */
@Component
@Slf4j
public class SendMsgListener {

    @Resource
    private MessageService messageService;
    @Resource
    private RoomService roomService;
    @Resource
    private RoomFriendService roomFriendService;
    @Resource
    private GroupMemberService groupMemberService;
    @Resource
    private MqService mqService;
    @Resource
    private ContactService contactService;
    @Resource
    private ChatService chatService;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MESSAGE_QUEUE_NAME,durable = "true"),
            exchange = @Exchange(name = MESSAGE_EXCHANGE_NAME),
            key = MESSAGE_KEY
    ))
    public void rcvSendMsgHandler(Long msgId){
        log.info("receive message send,the message id is:[{}]",msgId);
        //1. 根据这条消息的id以及创建时间来更新room表中的最后一条消息的id以及最后一条消息的时间两个信息。
        Message message = messageService.getById(msgId);
        Room room = roomService.getById(message.getRoomId());
        if(Objects.isNull(message)){
            log.error("receive messageId,but this message is null.the message id is:[{}]",msgId);
            return;
        }
        roomService.updateLaseMessageInfo(message.getRoomId(),message.getId(),message.getCreateTime());
        //2. 更新当前房间中所有人（每个uid和roomId对应一个唯一的会话）中的所有的会话的最后一条消息id和时间，若是没有会话的话就直接插入即可。
        List<Long> roomMemberUidList = new ArrayList<>();
        if(Objects.equals(room.getType(), RoomTypeEnum.FRIEND.getType())){
            //私聊
            RoomFriend roomFriend = roomFriendService.geyByRoomId(room.getId());
            roomMemberUidList = Arrays.asList(roomFriend.getUid1(),roomFriend.getUid2());
        }else{
            //群聊
            roomMemberUidList = groupMemberService.getGroupMemberList(room.getId());
        }
        contactService.refreshOrCreateActiveTime(room.getId(),roomMemberUidList,message.getId(),message.getCreateTime());
        //3. 将这条消息所封装的响应信息通过websocket通知给该房间里所有在线的用户（若是私聊房间就是推送给对应的两个用户）
        // 消息发送时传入uid为null,默认所有用户都没有点赞和举报
        ChatMessageResp msgResp = chatService.getMsgResp(null, msgId);
        mqService.sendPushMsg(new PushMsgDto(roomMemberUidList, WebSocketAdapter.buildMsgSendResp(msgResp)));
    }
}
