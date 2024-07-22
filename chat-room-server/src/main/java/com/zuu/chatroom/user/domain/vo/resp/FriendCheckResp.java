package com.zuu.chatroom.user.domain.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/20 19:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendCheckResp {

    @Schema(title = "校验结果")
    private List<FriendCheck> checkedList;

    @Data
    public static class FriendCheck {
        private Long uid;
        private Boolean isFriend;
    }

}
