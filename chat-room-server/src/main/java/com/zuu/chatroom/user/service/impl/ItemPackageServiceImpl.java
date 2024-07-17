package com.zuu.chatroom.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zuu.chatroom.common.annotation.RedissonLock;
import com.zuu.chatroom.common.domain.enums.IdempotentEnum;
import com.zuu.chatroom.common.domain.enums.YesOrNoEnum;
import com.zuu.chatroom.common.exception.BusinessException;
import com.zuu.chatroom.common.utils.LockUtil;
import com.zuu.chatroom.user.domain.po.ItemPackage;
import com.zuu.chatroom.user.mapper.ItemPackageMapper;
import com.zuu.chatroom.user.service.ItemPackageService;
import com.zuu.chatroom.user.service.adapter.ItemPackageAdapter;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
* @author zuu
* @description 针对表【item_package(用户背包表)】的数据库操作Service实现
* @createDate 2024-07-14 16:07:33
*/
@Service
public class ItemPackageServiceImpl extends ServiceImpl<ItemPackageMapper, ItemPackage>
    implements ItemPackageService {

    @Resource
    LockUtil lockUtil;
    @Override
    public int getCountByItemId(Long uid, Long itemId) {
        long count = this.count(new QueryWrapper<ItemPackage>()
                .eq("uid", uid)
                .eq("item_id", itemId)
                .eq("status", YesOrNoEnum.NO));
        return (int)count;
    }

    @Override
    public ItemPackage getFirstItem(Long uid, Long itemId) {
        return this.getOne(
                new QueryWrapper<ItemPackage>()
                        .eq("uid", uid)
                        .eq("item_id", itemId)
                        .eq("status", YesOrNoEnum.NO.getStatus()));
    }

    @Override
    public boolean useItem(Long id) {
        return this.update(new UpdateWrapper<ItemPackage>()
                .eq("id", id)
                .eq("status", YesOrNoEnum.NO.getStatus())
                .set("status", YesOrNoEnum.YES.getStatus()));
    }

    @Override
    public List<ItemPackage> getByUid(Long uid, List<Long> badgeIds) {
        return this.list(new QueryWrapper<ItemPackage>()
                .eq("uid", uid)
                .eq("status", YesOrNoEnum.NO.getStatus())
                .in("item_id", badgeIds)
        );
    }

    /**
     *
     * @param uid 用户id
     * @param itemId 物品id
     * @param idempotentEnum 幂等类型
     * @param businessId 上层发送的唯一标识
     */
    @Override
    public void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        String idempotent = getIdempotent(itemId,idempotentEnum,businessId);
        //通过代理类确保aop
        ItemPackageServiceImpl itemPackageService = (ItemPackageServiceImpl) AopContext.currentProxy();
        itemPackageService.doAcquire(uid,itemId,idempotent);

        //编程式
        /*lockUtil.executeWithLock("acquireItem:" + idempotent,() -> {
            ItemPackage itemPackage = this.getOne(new QueryWrapper<ItemPackage>().eq("idempotent", idempotent));
            if(Objects.nonNull(itemPackage)){
                //说明物品已经发放成功了，返回成功信息
                return;
            }
            //发放物品
            ItemPackage insertItem = ItemPackageAdapter.buildItemPackageInfo(uid,itemId,idempotent);
            this.save(insertItem);
        });*/
    }

    @RedissonLock(key = "#idempotent")
    @Transactional
    public void doAcquire(Long uid,Long itemId,String idempotent){
        ItemPackage itemPackage = this.getOne(new QueryWrapper<ItemPackage>().eq("idempotent", idempotent));
        if(Objects.nonNull(itemPackage)){
            //说明物品已经发放成功了，返回成功信息
            return;
        }
        //发放物品
        ItemPackage insertItem = ItemPackageAdapter.buildItemPackageInfo(uid,itemId,idempotent);
        this.save(insertItem);
    }


    private String getIdempotent(Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        return String.format("%d_%d_%s", itemId, idempotentEnum.getType(), businessId);
    }
}




