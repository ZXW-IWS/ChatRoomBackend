package com.zuu.chatroom.websocket.domain.vo.resp;

import com.zuu.chatroom.user.domain.po.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/9 11:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WsLoginSuccess {
    private Long id;
    private String avatar;
    private String token;
    private String name;

    public WsLoginSuccess(User user,String token){
        this.id = user.getId();
        this.avatar = user.getAvatar();
        this.name = user.getNickname();
        this.token = token;
    }
}
