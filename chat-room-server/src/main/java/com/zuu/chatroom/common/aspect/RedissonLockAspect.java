package com.zuu.chatroom.common.aspect;

import com.zuu.chatroom.common.annotation.RedissonLock;
import com.zuu.chatroom.common.utils.LockUtil;
import com.zuu.chatroom.common.utils.SpElUtils;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/15 18:48
 */
@Component
@Aspect
@Order(0) //保证锁的获取释放在事务的外层
public class RedissonLockAspect {
    @Resource
    private LockUtil lockUtil;

    /**
     * 注解@Around 表示在有@RedissonLock注解的方法前后执行该切面逻辑。
     * @param joinPoint
     * @return
     */
    @Around("@annotation(com.zuu.chatroom.common.annotation.RedissonLock)")
    public Object around(ProceedingJoinPoint joinPoint){
        //得到注解方法的签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //根据签名获取到方法
        Method method = methodSignature.getMethod();
        //获取该方法上的RedissonLock注解对象
        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);
        //获取prefix
        String prefix = redissonLock.prefix();
        //将key的el表达式解析出来
        //joinPoint.getArgs() 方法用于获取当前被拦截方法的参数
        String key = SpElUtils.parseEl(method, joinPoint.getArgs(), redissonLock.key());
        //proceed() 方法用于继续执行被拦截的方法，并返回其执行结果。
        return lockUtil.executeWithLockThrows(
                prefix+":"+key, redissonLock.waitTime(), redissonLock.timeUnit(),joinPoint::proceed);
    }

}
