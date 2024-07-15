package com.zuu.chatroom;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @Author zuu
 * @Description   启动类
 * @Date 2024/7/3 11:05
 */
@SpringBootApplication
@MapperScan(basePackages = "com.zuu.chatroom.**.mapper")
@EnableAspectJAutoProxy
public class ChatRoomApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatRoomApplication.class);
    }
}
