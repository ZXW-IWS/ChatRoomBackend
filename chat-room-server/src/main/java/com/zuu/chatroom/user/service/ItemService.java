package com.zuu.chatroom.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zuu.chatroom.user.domain.po.Item;

import java.util.List;

/**
* @author zuu
* @description 针对表【item(物品表)】的数据库操作Service
* @createDate 2024-07-14 16:05:41
*/
public interface ItemService extends IService<Item> {


    List<Item> getListByType(Integer type);
}
