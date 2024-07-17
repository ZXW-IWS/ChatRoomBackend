package com.zuu.chatroom.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zuu.chatroom.user.domain.po.Black;

import java.util.Map;
import java.util.Set;

/**
* @author zuu
* @description 针对表【black(黑名单)】的数据库操作Service
* @createDate 2024-07-17 09:15:08
*/
public interface BlackService extends IService<Black> {

    Map<Integer, Set<String>> getBlackMap();
}
