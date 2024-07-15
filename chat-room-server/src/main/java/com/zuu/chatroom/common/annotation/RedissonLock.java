package com.zuu.chatroom.common.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

import static com.zuu.chatroom.common.constant.RedisConstant.BASE_KEY;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/15 18:42
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RedissonLock {
    /**
     * 锁key的前缀，默认就是系统redis的base key.
     */
    String prefix() default BASE_KEY;

    /**
     * 锁的key
     */
    String key();

    /**
     * 等待获取锁最大时间，默认不等待
     */
    long waitTime() default -1;

    /**
     * 等待时间的单位，默认ms
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
