package com.zuu.chatroom.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/8 16:15
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WsChannelUserDto implements Serializable {
    private Long uid;
}
