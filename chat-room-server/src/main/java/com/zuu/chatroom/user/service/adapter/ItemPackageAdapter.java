package com.zuu.chatroom.user.service.adapter;

import com.zuu.chatroom.common.domain.enums.YesOrNoEnum;
import com.zuu.chatroom.user.domain.po.ItemPackage;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/15 16:09
 */
public class ItemPackageAdapter {
    public static ItemPackage buildItemPackageInfo(Long uid, Long itemId, String idempotent) {
        ItemPackage itemPackage = new ItemPackage();
        itemPackage.setUid(uid);
        itemPackage.setItemId(itemId);
        itemPackage.setIdempotent(idempotent);
        itemPackage.setStatus(YesOrNoEnum.NO.getStatus());
        return itemPackage;
    }
}
