package com.zuu.chatroom;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.zuu.chatroom.common.domain.enums.IdempotentEnum;
import com.zuu.chatroom.common.utils.RedisUtils;
import com.zuu.chatroom.user.domain.entity.IpDetail;
import com.zuu.chatroom.user.domain.enums.ItemEnum;
import com.zuu.chatroom.user.domain.enums.ItemTypeEnum;
import com.zuu.chatroom.user.domain.po.Black;
import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.user.service.IpService;
import com.zuu.chatroom.user.service.ItemPackageService;
import com.zuu.chatroom.user.service.UserService;
import com.zuu.chatroom.user.service.impl.IpServiceImpl;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/9 14:57
 */
@SpringBootTest
@Slf4j
class ChatRoomApplicationTest {
    @Resource
    UserService userService;
    @Resource
    ItemPackageService itemPackageService;
    @Resource
    RabbitTemplate rabbitTemplate;
    @Resource
    IpServiceImpl ipService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    public static final long UID = 1L;

    @Test
    public void login(){
        String token = userService.login(4L);
        System.out.println("token = " + token);
    }
    
    @Test
    public void hutoolTest(){
        //String idStr = RedisUtils.get("chat-room:token:6a7b8b2186654ad493072b8169830b4f", String.class);
        //long id = Long.parseLong(idStr);
        Long id = RedisUtils.get("chat-room:token:6a7b8b2186654ad493072b8169830b4f", Long.class);
        System.out.println("id = " + id);

    }

    @Test
    public void threadTest(){
        Thread thread =new Thread(()->{
            try{
                log.info("111");
                throw new RuntimeException("运行时异常了");
            }catch (Exception e){
                log.error("异常发生",e);
            }
        });
        thread.start();
    }

    @Test
    public void splitTest(){
        String uri = "localhost:8080/user/public/abcd";
        String[] split = uri.split("/");
        for (String s : split) {
            System.out.println( s);
        }
    }

    @Test
    public void acquireItemTest(){
        itemPackageService.acquireItem(UID, ItemEnum.REG_TOP100_BADGE.getId(), IdempotentEnum.UID,UID+"");
    }

    @Test
    public void sendMqTest()  {
        rabbitTemplate.convertAndSend("test.queue",new User());
    }
    @Test
    public void registerMqTest() throws InterruptedException {
        userService.register("12345");
        Thread.sleep(1000);
    }

    @Test
    public void ipMqTest() throws InterruptedException {

        Thread.sleep(1000);
    }

    @Test
    public void insertTest(){
        User user = new User();
        user.setId(2L);
        user.setActiveStatus(1);
        userService.updateById(user);
    }

    ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(100));
    @Test
    public void getIpTest() throws InterruptedException {
        Date begin = new Date();
        for (int i = 0; i < 50; i++) {
            int finalI = i+1;
            executor.execute(() -> {
                String ip = "182.92.80.155";
                IpDetail ipDetail = ipService.tryGetIpDetail(ip);
                if(Objects.nonNull(ipDetail)){
                    Date date = new Date();
                    System.out.println("第" + finalI +"次获取成功，耗时" + (date.getTime()-begin.getTime()) + "ms" + ipDetail);
                }
            });
        }
        Thread.sleep(10 * 60 * 1000);
    }
    
    @Test
    public void listRedisTest(){
        String s = stringRedisTemplate.opsForValue().get("empty");
        List<Black> list = JSONUtil.toList(s, Black.class);
        for (Black black : list) {
            System.out.println("black = " + black);
        }
        list.stream().collect(Collectors.groupingBy(Black::getType));
    }
}