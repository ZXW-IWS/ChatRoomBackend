package com.zuu.chatroom.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuu.chatroom.chat.domain.po.Contact;
import com.zuu.chatroom.chat.mapper.ContactMapper;
import com.zuu.chatroom.chat.service.ContactService;
import org.springframework.stereotype.Service;

/**
* @author zuu
* @description 针对表【contact(会话列表)】的数据库操作Service实现
* @createDate 2024-07-22 14:15:04
*/
@Service
public class ContactServiceImpl extends ServiceImpl<ContactMapper, Contact>
    implements ContactService {

}




