package com.zuu.chatroom.user.domain.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/18 20:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendApplyResp {
    @Schema(title = "申请id")
    private Long applyId;

    @Schema(title = "申请人uid")
    private Long uid;

    @Schema(title = "申请类型 1加好友")
    private Integer type;

    @Schema(title = "申请信息")
    private String msg;

    @Schema(title = "申请状态 1待审批 2同意")
    private Integer status;
}
