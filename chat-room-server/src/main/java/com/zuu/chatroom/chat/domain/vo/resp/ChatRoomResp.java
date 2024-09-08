package com.zuu.chatroom.chat.domain.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomResp {
    @Schema(title ="房间id")
    private Long roomId;
    @Schema(title ="房间类型 1群聊 2单聊")
    private Integer type;
    @Schema(title ="是否全员展示的会话 0否 1是")
    private Integer hot_Flag;
    @Schema(title ="最新消息")
    private String text;
    @Schema(title ="会话名称")
    private String name;
    @Schema(title ="会话头像")
    private String avatar;
    @Schema(title ="房间最后活跃时间(用来排序)")
    private Date activeTime;
    @Schema(title ="未读数")
    private Integer unreadCount;
}