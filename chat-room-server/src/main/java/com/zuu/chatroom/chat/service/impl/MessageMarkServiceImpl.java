package com.zuu.chatroom.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuu.chatroom.chat.domain.enums.MarkedMsgStatusEnum;
import com.zuu.chatroom.chat.domain.po.Message;
import com.zuu.chatroom.chat.domain.po.MessageMark;
import com.zuu.chatroom.chat.mapper.MessageMarkMapper;
import com.zuu.chatroom.chat.service.MessageMarkService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author zuu
* @description 针对表【message_mark(消息标记表)】的数据库操作Service实现
* @createDate 2024-07-22 13:55:00
*/
@Service
public class MessageMarkServiceImpl extends ServiceImpl<MessageMarkMapper, MessageMark>
    implements MessageMarkService {

    @Override
    public List<MessageMark> getMarksByMsgId(Long msgId) {
        return this.list(new QueryWrapper<MessageMark>()
                .eq("msg_id", msgId)
                .eq("status", MarkedMsgStatusEnum.NORMAL.getStatus()));
    }

    @Override
    public List<MessageMark> getMarksByBatchMsgId(List<Message> messageList) {
        List<Long> msgIdList = messageList.stream().map(Message::getId).toList();

        return this.list(
                new QueryWrapper<MessageMark>()
                        .eq("status", MarkedMsgStatusEnum.NORMAL.getStatus())
                        .in("msg_id", msgIdList));
    }
}




