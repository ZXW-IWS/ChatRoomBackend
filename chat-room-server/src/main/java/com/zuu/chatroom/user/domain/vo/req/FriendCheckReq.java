package com.zuu.chatroom.user.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/20 19:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendCheckReq {
    @NotEmpty
    @Size(max = 50)
    @Schema(title = "校验好友的uid")
    private List<Long> uidList;
}
