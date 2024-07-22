package com.zuu.chatroom.common.utils;

import com.zuu.chatroom.common.exception.BusinessException;
import com.zuu.chatroom.common.exception.CommonErrorEnum;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/15 17:10
 */
@Component
public class LockUtil {
    @Resource
    RedissonClient redisson;

    public <T> T executeWithLockThrows(String key, long time, TimeUnit timeUnit, ThrowableSupplier<T> supplier){
        RLock lock = redisson.getLock(key);
        try{
            boolean locked = lock.tryLock(time, timeUnit);
            if(!locked){
                throw new BusinessException(CommonErrorEnum.LOCK_ERROR);
            }
            //执行业务逻辑
            return supplier.get();
        }catch (BusinessException e){
            throw new BusinessException(e);
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
    public <T> T executeWithLock(String key, long time, TimeUnit timeUnit, Supplier<T> supplier){
        RLock lock = redisson.getLock(key);
        try{
            boolean locked = lock.tryLock(time, timeUnit);
            if(!locked){
                throw new BusinessException(CommonErrorEnum.LOCK_ERROR);
            }
            //执行业务逻辑
            return supplier.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public <T> T executeWithLock(String key, Supplier<T> supplier){
        return executeWithLock(key,-1,TimeUnit.MILLISECONDS,supplier);
    }

    public <T> T executeWithLock(String key, Runnable runnable){
        return executeWithLock(key,() -> {
            runnable.run();
            return null;
        });
    }

    @FunctionalInterface
    public interface ThrowableSupplier<T>{
        T get() throws Throwable;
    }
}
