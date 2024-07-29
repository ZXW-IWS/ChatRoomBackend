package com.zuu.chatroom.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zuu.chatroom.chat.domain.po.Room;

import java.util.Date;

/**
* @author zuu
* @description 针对表【room(房间表)】的数据库操作Service
* @createDate 2024-07-22 14:15:04
*/
public interface RoomService extends IService<Room> {

    /**
     * 更新房间的最后一条消息id和发送时间
     */
    void updateLaseMessageInfo(Long roomId, Long msgId, Date msgSendTime);
}
