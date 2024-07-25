package com.zuu.chatroom.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zuu.chatroom.chat.domain.po.MessageMark;

import java.util.List;

/**
* @author zuu
* @description 针对表【message_mark(消息标记表)】的数据库操作Service
* @createDate 2024-07-22 13:55:00
*/
public interface MessageMarkService extends IService<MessageMark> {

    List<MessageMark> getMarksByMsgId(Long msgId);
}
