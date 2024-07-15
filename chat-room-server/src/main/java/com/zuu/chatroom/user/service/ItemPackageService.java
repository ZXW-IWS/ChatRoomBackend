package com.zuu.chatroom.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zuu.chatroom.user.domain.po.ItemPackage;

/**
* @author zuu
* @description 针对表【item_package(用户背包表)】的数据库操作Service
* @createDate 2024-07-14 16:07:33
*/
public interface ItemPackageService extends IService<ItemPackage> {

    int getCountByItemId(Long uid, Long itemId);
    
    ItemPackage getFirstItem(Long uid, Long itemId);

    boolean useItem(Long id);
}
