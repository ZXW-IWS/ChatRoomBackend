package com.zuu.chatroom.user.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuu.chatroom.common.utils.JsonUtils;
import com.zuu.chatroom.user.domain.po.Black;
import com.zuu.chatroom.user.mapper.BlackMapper;
import com.zuu.chatroom.user.service.BlackService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.zuu.chatroom.common.constant.RedisConstant.BLACK_LIST_KEY;
import static com.zuu.chatroom.common.constant.RedisConstant.BLACK_LIST_TTL_MINUTES;

/**
* @author zuu
* @description 针对表【black(黑名单)】的数据库操作Service实现
* @createDate 2024-07-17 09:15:08
*/
@Service
public class BlackServiceImpl extends ServiceImpl<BlackMapper, Black>
    implements BlackService {

    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Override
    public Map<Integer, Set<String>> getBlackMap() {
        //1. 从redis中获取黑名单列表
        String blackListStr = stringRedisTemplate.opsForValue().get(BLACK_LIST_KEY);
        List<Black> blackList = JSONUtil.toList(blackListStr, Black.class);
        long count = this.count();
        //2.若不存在或黑名单列表长度与数据库不一致，则从数据库中获取并更新redis
        if(Objects.isNull(blackList) || count != blackList.size()){
            blackList = this.list();
            stringRedisTemplate.opsForValue()
                    .set(BLACK_LIST_KEY,JSONUtil.toJsonStr(blackList),BLACK_LIST_TTL_MINUTES, TimeUnit.MINUTES);
        }
        //3. 将黑名单list转为type->set<target>的形式返回
        return generateBlackMap(blackList);
    }

    private Map<Integer, Set<String>> generateBlackMap(List<Black> blackList) {
        Map<Integer, List<Black>> collect = blackList.stream().collect(Collectors.groupingBy(Black::getType));
        Map<Integer,Set<String>> res = new HashMap<>(collect.size());
        //组装结果
        collect.forEach((key,value) -> {
            Set<String> valueSet = value.stream().map(Black::getTarget).collect(Collectors.toSet());
            res.put(key,valueSet);
        });

        return res;
    }
}




