package com.zuu.chatroom.websocket.domain.vo.req;

import lombok.Data;

/**
 * @Author zuu
 * @Description ws基本请求
 * @Date 2024/7/3 15:12
 */
@Data
public class WsBaseReq {
    /**
     * @see com.zuu.chatroom.websocket.domain.enums.WsBaseReqTypeEnum
     */
    private Integer type;
    /**
     * 请求携带的数据
     */
    private String data;
}
