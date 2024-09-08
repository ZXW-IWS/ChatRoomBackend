package com.zuu.chatroom.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuu.chatroom.chat.domain.po.Message;
import com.zuu.chatroom.chat.mapper.MessageMapper;
import com.zuu.chatroom.chat.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
* @author zuu
* @description 针对表【message(消息表)】的数据库操作Service实现
* @createDate 2024-07-22 13:55:00
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService {

    @Override
    public Integer getUnreadCount(Long roomId, Long lastMsgId) {
        return (int) this.count(
                new QueryWrapper<Message>()
                        .eq("room_id", roomId)
                        .gt(Objects.nonNull(lastMsgId),"id", lastMsgId));
    }
}




