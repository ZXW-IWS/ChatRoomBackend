package com.zuu.chatroom.chat.domain.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberResp {
    @Schema(title ="房间id")
    private Long roomId;
    @Schema(title ="群名称")
    private String groupName;
    @Schema(title ="群头像")
    private String avatar;
    @Schema(title ="在线人数")
    private Long onlineNum;
    /**
     * @see com.zuu.chatroom.chat.domain.enums.GroupRoleEnum
     */
    @Schema(title ="成员角色 1群主 2管理员 3普通成员 4已退出群聊")
    private Integer role;
}