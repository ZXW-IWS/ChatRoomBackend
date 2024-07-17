package com.zuu.chatroom.user.listener;

import com.zuu.chatroom.common.domain.enums.IdempotentEnum;
import com.zuu.chatroom.user.domain.enums.ItemEnum;
import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.user.service.ItemPackageService;
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
 * @Date 2024/7/16 11:33
 */
@Component
@Slf4j
public class RegisterListener {

    @Resource
    ItemPackageService itemPackageService;
    @Resource
    UserService userService;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = REGISTER_ACQUIRE_QUEUE_NAME,durable = "true")
            ,exchange = @Exchange(name = REGISTER_EXCHANGE_NAME)
            ,key = REGISTER_KEY))
    public void acquireItemMessageHandler(User user){
        log.info("receive user register,the user is:[{}]",user);
        //1. 发送改名卡
        log.info("acquire modify name card to user:[{}]",user);
        itemPackageService.acquireItem(user.getId(), ItemEnum.MODIFY_NAME_CARD.getId(), IdempotentEnum.UID,user.getId().toString());
        //2. 发送注册徽章
        long count = userService.count();
        if(count <= 10){
            itemPackageService.acquireItem(user.getId(),ItemEnum.REG_TOP10_BADGE.getId(), IdempotentEnum.UID,user.getId().toString());
            log.info("acquire REG_TOP10_BADGE to user:[{}]",user);
        }
        if(count <= 100){
            itemPackageService.acquireItem(user.getId(),ItemEnum.REG_TOP100_BADGE.getId(), IdempotentEnum.UID,user.getId().toString());
            log.info("acquire REG_TOP100_BADGE to user:[{}]",user);
        }

    }
}
