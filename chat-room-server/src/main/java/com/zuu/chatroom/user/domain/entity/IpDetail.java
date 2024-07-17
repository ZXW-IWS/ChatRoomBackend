package com.zuu.chatroom.user.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 *  @Author zuu
 *  @Description 
 *  @Date 2024/7/16 16:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IpDetail implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String ip;
    /**
     * 运营商信息
     */
    private String isp;
    private String isp_id;
    /**
     * 城市信息
     */
    private String city;
    private String city_id;
    private String country;
    private String country_id;
    private String region;
    private String region_id;
}
