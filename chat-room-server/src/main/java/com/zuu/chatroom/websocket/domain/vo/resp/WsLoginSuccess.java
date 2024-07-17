package com.zuu.chatroom.websocket.domain.vo.resp;

import com.zuu.chatroom.common.domain.enums.YesOrNoEnum;
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
    //用户权限 0普通用户 1超管
    private Integer power;
    public WsLoginSuccess(User user, String token, boolean hasPower){
        this.id = user.getId();
        this.avatar = user.getAvatar();
        this.name = user.getNickname();
        this.token = token;
        this.power = hasPower ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus();
    }
}
