package com.zuu.chatroom;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.zuu.chatroom.common.domain.enums.IdempotentEnum;
import com.zuu.chatroom.common.utils.RedisUtils;
import com.zuu.chatroom.user.domain.enums.ItemEnum;
import com.zuu.chatroom.user.domain.enums.ItemTypeEnum;
import com.zuu.chatroom.user.service.ItemPackageService;
import com.zuu.chatroom.user.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static final long UID = 1L;

    @Test
    public void login(){
        String token = userService.login(1L);
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
}