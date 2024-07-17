package com.zuu.chatroom.user.domain.entity;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 *  @Author zuu
 *  @Description 
 *  @Date 2024/7/16 16:22
 */
@Data
public class IpInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    //注册时的ip
    private String createIp;
    //注册时的ip详情
    private IpDetail createIpDetail;
    //最新登录的ip
    private String updateIp;
    //最新登录的ip详情
    private IpDetail updateIpDetail;

    public void refreshIp(String ip) {
        if(StrUtil.isBlank(ip))
            return;

        updateIp = ip;
        if(StrUtil.isBlank(createIp))
            createIp = ip;
    }

    /**
     * 获取需要更新的ip信息，检测updateIpDetail中的ip与updateIp是否一致就可以确定是否需要更新
     * 若需要更新，则返回updateIp,不需要更新则直接返回null即可
     * 若createIp也需要更新，此时createIp与updateIp是一致的，也就是需要更新的ip是一样的，
     * 因此也可以直接返回updateIp即可，后续更新ipDetail的时候通过判断updateIp和createIp与当前函数的ip是否一致进行更新
     * @return
     */
    public String needUpdateIp() {
        if(Objects.isNull(updateIpDetail))
            return updateIp;
        //若detail中的ip与updateIp一致也不需要更新类
        if(!updateIpDetail.getIp().equals(updateIp))
            return updateIp;
        return null;
    }

    public void refreshIpDetail(IpDetail ipDetail) {
        if(ipDetail.getIp().equals(createIp))
            createIpDetail = ipDetail;
        if(ipDetail.getIp().equals(updateIp))
            updateIpDetail = ipDetail;
    }
}
