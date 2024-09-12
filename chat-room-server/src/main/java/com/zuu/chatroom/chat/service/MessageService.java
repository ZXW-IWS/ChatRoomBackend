package com.zuu.chatroom.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zuu.chatroom.chat.domain.po.Message;

/**
* @author zuu
* @description 针对表【message(消息表)】的数据库操作Service
* @createDate 2024-07-22 13:55:00
*/
public interface MessageService extends IService<Message> {

    Integer getUnreadCount(Long roomId, Long lastMsgId);

    void delRoomMsg(Long roomId);
}
