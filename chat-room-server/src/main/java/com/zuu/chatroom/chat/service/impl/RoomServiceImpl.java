package com.zuu.chatroom.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuu.chatroom.chat.domain.po.Room;
import com.zuu.chatroom.chat.mapper.RoomMapper;
import com.zuu.chatroom.chat.service.RoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
* @author zuu
* @description 针对表【room(房间表)】的数据库操作Service实现
* @createDate 2024-07-22 14:15:04
*/
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room>
    implements RoomService {

    @Override
    @Transactional
    public void updateLaseMessageInfo(Long roomId, Long msgId, Date msgSendTime) {
        this.update(
                new UpdateWrapper<Room>()
                        .eq("id",roomId)
                        .set("last_msg_id",msgId)
                        .set("active_time",msgSendTime));
    }
}




