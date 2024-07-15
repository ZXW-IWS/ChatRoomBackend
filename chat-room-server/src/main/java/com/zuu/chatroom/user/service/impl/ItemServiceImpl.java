package com.zuu.chatroom.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zuu.chatroom.user.domain.po.Item;
import com.zuu.chatroom.user.mapper.ItemMapper;
import com.zuu.chatroom.user.service.ItemService;
import org.springframework.stereotype.Service;

/**
* @author zuu
* @description 针对表【item(物品表)】的数据库操作Service实现
* @createDate 2024-07-14 16:05:41
*/
@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item>
    implements ItemService {

}




