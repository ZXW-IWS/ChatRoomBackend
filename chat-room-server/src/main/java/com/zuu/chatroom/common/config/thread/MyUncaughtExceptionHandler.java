package com.zuu.chatroom.common.config.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/9 21:31
 */
@Slf4j
public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Exception in thread \"{}\" \n",t.getName(),e);
    }
}
