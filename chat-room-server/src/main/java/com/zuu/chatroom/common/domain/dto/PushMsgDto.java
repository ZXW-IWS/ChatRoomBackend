package com.zuu.chatroom.common.domain.dto;

import com.zuu.chatroom.common.domain.enums.PushMsgTypeEnum;
import com.zuu.chatroom.websocket.domain.vo.resp.WsBaseResp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/26 12:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushMsgDto {
    /**
     * 推送类型 全员或指定的用户列表
     */
    private Integer pushType;
    /**
     * 推送用户列表
     */
    private List<Long> uidList;
    /**
     * 推送的具体信息
     */
    private WsBaseResp wsBaseResp;

    public PushMsgDto(List<Long> uidList,WsBaseResp wsBaseResp){
        this.pushType = PushMsgTypeEnum.USER.getType();
        this.uidList = uidList;
        this.wsBaseResp = wsBaseResp;
    }
}
