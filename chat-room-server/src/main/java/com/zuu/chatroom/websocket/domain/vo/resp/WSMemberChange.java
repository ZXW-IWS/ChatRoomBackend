package com.zuu.chatroom.websocket.domain.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WSMemberChange {

    @Schema(title ="群组id")
    private Long roomId;
    @Schema(title ="变动uid")
    private Long uid;
    @Schema(title ="变动类型 1加入群组 2移除群组")
    private Integer changeType;
    /**
     * @see com.zuu.chatroom.user.domain.enums.UserActiveStatusEnum
     */
    @Schema(title ="在线状态 1在线 2离线")
    private Integer activeStatus;
    @Schema(title ="最后一次上下线时间")
    private Date lastOptTime;
}