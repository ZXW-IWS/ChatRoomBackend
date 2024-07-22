package com.zuu.chatroom.user.domain.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendUnreadResp {

    @Schema(title = "申请列表的未读数")
    private Long unReadCount;

}