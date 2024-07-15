package com.zuu.chatroom.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zuu.chatroom.common.domain.enums.IdempotentEnum;
import com.zuu.chatroom.user.domain.po.ItemPackage;

import java.util.List;

/**
* @author zuu
* @description 针对表【item_package(用户背包表)】的数据库操作Service
* @createDate 2024-07-14 16:07:33
*/
public interface ItemPackageService extends IService<ItemPackage> {

    /**
     * 获取改名卡数量
     * @return
     */
    int getCountByItemId(Long uid, Long itemId);


    ItemPackage getFirstItem(Long uid, Long itemId);

    /**
     * 使用改名卡
     */
    boolean useItem(Long id);

    /**
     * 获取用户的徽章
     */
    List<ItemPackage> getByUid(Long uid, List<Long> badgeIds);

    /**
     * 给用户发送物品
     * @param uid 用户id
     * @param itemId 物品id
     * @param idempotentEnum 幂等类型
     * @param businessId 上层发送的唯一标识
     */
    void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum,String businessId);
}
