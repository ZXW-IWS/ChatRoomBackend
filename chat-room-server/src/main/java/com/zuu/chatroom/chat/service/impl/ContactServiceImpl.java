package com.zuu.chatroom.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuu.chatroom.chat.domain.po.Contact;
import com.zuu.chatroom.chat.mapper.ContactMapper;
import com.zuu.chatroom.chat.service.ContactService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
* @author zuu
* @description 针对表【contact(会话列表)】的数据库操作Service实现
* @createDate 2024-07-22 14:15:04
*/
@Service
public class ContactServiceImpl extends ServiceImpl<ContactMapper, Contact>
    implements ContactService {

    @Override
    @Transactional
    public void refreshOrCreateActiveTime(Long roomId, List<Long> roomMemberUidList, Long msgId, Date msgSendTime) {
        this.baseMapper.refreshOrCreateActiveTime(roomId,roomMemberUidList,msgId,msgSendTime);
    }

    @Override
    public Long getContactLastMsgId(Long roomId, Long uid) {
        return this.getOne(
                new QueryWrapper<Contact>()
                        .eq("uid", uid)
                        .eq("room_id", roomId))
                .getLastMsgId();
    }
}




