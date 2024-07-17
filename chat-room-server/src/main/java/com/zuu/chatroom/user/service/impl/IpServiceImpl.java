package com.zuu.chatroom.user.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.zuu.chatroom.user.domain.dto.IpResult;
import com.zuu.chatroom.user.domain.entity.IpDetail;
import com.zuu.chatroom.user.domain.entity.IpInfo;
import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.user.service.IpService;
import com.zuu.chatroom.user.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/16 17:18
 */
@Service
@Slf4j
public class IpServiceImpl implements IpService {

    public static final int GET_IP_DETAIL_RETRY_TIME = 3;
    @Resource
    private UserService userService;
    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(500),
            new NamedThreadFactory("refresh-ipDetail", null, false));
    @Override
    public void refreshIpDetailAsync(Long id) {
        EXECUTOR.execute(() -> {
            User user = userService.getById(id);
            IpInfo ipInfo = user.getIpInfo();
            if(Objects.isNull(ipInfo)){
                return;
            }
            String ip = ipInfo.needUpdateIp();
            if(StrUtil.isBlank(ip))
                return;
            IpDetail ipDetail = tryGetIpDetail(ip);
            if(Objects.nonNull(ipDetail)){
                //更新ipDetail
                ipInfo.refreshIpDetail(ipDetail);
                User updateUser = new User();
                updateUser.setId(id);
                updateUser.setIpInfo(ipInfo);
                userService.updateById(updateUser);
                log.info("get ip success,the userId is:[{}],ipInfo is:[{}]",id,ipInfo);
            }else{
                log.error("get ip detail error,the ip is:[{}],the user is:[{}]",ip,user);
            }
        });
    }

    public IpDetail tryGetIpDetail(String ip) {
        //利用淘宝url获取ip的详细信息，若失败则重试，最多3次
        for (int i = 0; i < GET_IP_DETAIL_RETRY_TIME; i++) {
            IpDetail ipDetail = getIpDetail(ip);
            if(Objects.nonNull(ipDetail))
                return ipDetail;
            //若请求接口失败，则等待一段时间再次获取
            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    public IpDetail getIpDetail(String ip) {
        String body = HttpUtil.get("https://ip.taobao.com/outGetIpInfo?ip=" + ip + "&accessKey=alibaba-inc");
        IpResult<IpDetail> result = JSONUtil.toBean(body, new TypeReference<IpResult<IpDetail>>() {},
                false);
        if(result.isSuccess())
            return result.getData();
        return null;

    }
}
