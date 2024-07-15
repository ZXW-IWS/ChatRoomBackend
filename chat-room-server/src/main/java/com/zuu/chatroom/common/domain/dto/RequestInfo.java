package com.zuu.chatroom.common.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/14 15:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestInfo {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 访问用户ip
     */
    private String clientIp;
}
