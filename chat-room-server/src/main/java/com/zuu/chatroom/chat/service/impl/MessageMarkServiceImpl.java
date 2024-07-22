package com.zuu.chatroom.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuu.chatroom.chat.domain.po.MessageMark;
import com.zuu.chatroom.chat.mapper.MessageMarkMapper;
import com.zuu.chatroom.chat.service.MessageMarkService;
import org.springframework.stereotype.Service;

/**
* @author zuu
* @description 针对表【message_mark(消息标记表)】的数据库操作Service实现
* @createDate 2024-07-22 13:55:00
*/
@Service
public class MessageMarkServiceImpl extends ServiceImpl<MessageMarkMapper, MessageMark>
    implements MessageMarkService {

}




