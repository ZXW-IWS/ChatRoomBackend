package com.zuu.chatroom.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zuu.chatroom.chat.domain.po.Contact;

import java.util.Date;
import java.util.List;

/**
* @author zuu
* @description 针对表【contact(会话列表)】的数据库操作Service
* @createDate 2024-07-22 14:15:04
*/
public interface ContactService extends IService<Contact> {

    void refreshOrCreateActiveTime(Long roomId, List<Long> roomMemberUidList, Long msgId, Date msgSendTime);

    Long getContactLastMsgId(Long roomId, Long uid);
}
