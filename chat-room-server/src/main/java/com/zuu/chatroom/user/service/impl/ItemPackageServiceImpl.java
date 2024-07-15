package com.zuu.chatroom.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zuu.chatroom.common.domain.enums.YesOrNoEnum;
import com.zuu.chatroom.user.domain.po.ItemPackage;
import com.zuu.chatroom.user.mapper.ItemPackageMapper;
import com.zuu.chatroom.user.service.ItemPackageService;
import org.springframework.stereotype.Service;

/**
* @author zuu
* @description 针对表【item_package(用户背包表)】的数据库操作Service实现
* @createDate 2024-07-14 16:07:33
*/
@Service
public class ItemPackageServiceImpl extends ServiceImpl<ItemPackageMapper, ItemPackage>
    implements ItemPackageService {

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
}




