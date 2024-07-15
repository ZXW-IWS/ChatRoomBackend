package com.zuu.chatroom.user.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zuu.chatroom.common.utils.JsonUtils;
import com.zuu.chatroom.common.utils.RedisUtils;
import com.zuu.chatroom.user.domain.po.Item;
import com.zuu.chatroom.user.mapper.ItemMapper;
import com.zuu.chatroom.user.service.ItemService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zuu.chatroom.common.constant.RedisConstant.BADGE_LIST_KEY;
import static com.zuu.chatroom.common.constant.RedisConstant.BADGE_LIST_TTL_MINUTES;

/**
* @author zuu
* @description 针对表【item(物品表)】的数据库操作Service实现
* @createDate 2024-07-14 16:05:41
*/
@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item>
    implements ItemService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<Item> getListByType(Integer type) {
        //1. 先看redis中是否存在
        String badgeListStr = stringRedisTemplate.opsForValue().get(BADGE_LIST_KEY);
        if(StrUtil.isNotBlank(badgeListStr)){
            //2. 存在则直接返回
            return JSONUtil.toList(badgeListStr, Item.class);
        }
        //3.若redis中不存在，则从数据库中获取并同步到redis中
        List<Item> itemList = this.list(new QueryWrapper<Item>().eq("type", type));
        //同步到redis中
        String itemListStr = JSONUtil.toJsonStr(itemList);
        stringRedisTemplate.opsForValue().set(BADGE_LIST_KEY,itemListStr,BADGE_LIST_TTL_MINUTES, TimeUnit.MINUTES);

        return itemList;
    }
}




