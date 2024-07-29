package com.zuu.chatroom.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zuu.chatroom.chat.domain.po.Contact;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* @author zuu
* @description 针对表【contact(会话列表)】的数据库操作Mapper
* @createDate 2024-07-22 14:15:04
* @Entity generator.domain.Contact
*/
public interface ContactMapper extends BaseMapper<Contact> {
    void refreshOrCreateActiveTime(@Param("roomId") Long roomId, @Param("memberUidList") List<Long> memberUidList, @Param("msgId") Long msgId, @Param("activeTime") Date activeTime);
}




