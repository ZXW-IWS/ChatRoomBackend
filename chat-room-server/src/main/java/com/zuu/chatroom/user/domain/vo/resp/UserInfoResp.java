package com.zuu.chatroom.user.domain.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/14 16:26
 */
@Data
public class UserInfoResp {
    @Schema(title = "用户id")
    private Long id;

    @Schema(title = "用户昵称")
    private String nickname;

    @Schema(title = "用户头像")
    private String avatar;

    @Schema(title = "性别 1为男性，2为女性")
    private Integer sex;

    @Schema(title = "剩余改名次数")
    private Integer modifyNameChance;
}
