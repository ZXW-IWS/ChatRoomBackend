package com.zuu.chatroom.chat.domain.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMemberResp {
    @Schema(title ="uid")
    private Long uid;
    /**
     * @see com.zuu.chatroom.user.domain.enums.UserActiveStatusEnum
     */
    @Schema(title ="在线状态 1在线 2离线")
    private Integer activeStatus;

    /**
     * 角色ID
     */
    private Integer roleId;

    @Schema(title ="最后一次上下线时间")
    private Date lastOptTime;
}