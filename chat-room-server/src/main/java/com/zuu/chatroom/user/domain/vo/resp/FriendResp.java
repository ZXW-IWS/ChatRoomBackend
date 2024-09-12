package com.zuu.chatroom.user.domain.vo.resp;

import com.zuu.chatroom.user.domain.enums.UserActiveStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/18 10:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendResp {

    @Schema(title = "好友uid")
    private Long uid;
    /**
     * @see UserActiveStatusEnum
     */
    @Schema(title = "在线状态 1在线 2离线")
    private Integer activeStatus;
}
