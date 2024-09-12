package com.zuu.chatroom.user.listener;

import com.zuu.chatroom.user.domain.enums.UserActiveStatusEnum;
import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.user.service.IpService;
import com.zuu.chatroom.user.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.zuu.chatroom.common.constant.RabbitMqConstant.*;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/16 16:56
 */
@Component
@Slf4j
public class OnlineListener {

    @Resource
    private UserService userService;
    @Resource
    private IpService ipService;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = ONLINE_QUEUE_NAME,durable = "true"),
            exchange = @Exchange(name = ONLINE_EXCHANGE_NAME),
            key = ONLINE_KEY
    ))
    public void onlineMsgHandler(User user){
        log.info("receive user online message,the user is:[{}]",user);
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setLastLoginTime(user.getLastLoginTime());
        updateUser.setIpInfo(user.getIpInfo());
        updateUser.setActiveStatus(UserActiveStatusEnum.ONLINE.getType());
        //保存到数据库中
        userService.updateById(updateUser);
        //异步更新用户的详细ip信息
        ipService.refreshIpDetailAsync(updateUser.getId());
    }
}
