package com.zuu.chatroom.websocket.domain.vo.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/6 12:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WsQrcodeResp {
    String qrcodeUrl;
}
