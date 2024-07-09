package com.zuu.chatroom.websocket.domain.vo.resp;

import lombok.Data;

/**
 * @Author zuu
 * @Description websocket推送给前端的消息格式
 * @Date 2024/7/6 11:30
 */
@Data
public class WsBaseResp {
    private Integer type;
    private Object data;
}
