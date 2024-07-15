package com.zuu.chatroom.user.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.zuu.chatroom.common.domain.enums.YesOrNoEnum;
import com.zuu.chatroom.user.domain.po.Item;
import com.zuu.chatroom.user.domain.po.ItemPackage;
import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.user.domain.vo.resp.BadgeResp;
import com.zuu.chatroom.user.domain.vo.resp.UserInfoResp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/14 16:40
 */
public class UserAdapter {
    public static UserInfoResp buildUserInfoResp(User user, int modifyNameChance) {
        UserInfoResp userInfoResp = new UserInfoResp();
        BeanUtil.copyProperties(user,userInfoResp);
        userInfoResp.setModifyNameChance(modifyNameChance);
        return userInfoResp;
    }

    /**
     * 构建徽章列表返回信息
     */
    public static List<BadgeResp> buildBadgeListResp(List<Item> badgeList, List<ItemPackage> packages, User user) {
        Set<Long> backgeIdSet = packages.stream().map(ItemPackage::getItemId).collect(Collectors.toSet());
        //先通过map来将信息组装成BadgeResp的对象，再通过是否佩戴以及是否拥有进行排序
        return badgeList.stream().map(badge -> {
            BadgeResp badgeResp = new BadgeResp();
            BeanUtil.copyProperties(badge, badgeResp);
            badgeResp.setObtain(
                    backgeIdSet.contains(badge.getId())
                            ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus()
            );
            badgeResp.setWearing(
                    badge.getId().equals(user.getItemId())
                            ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus()
            );
            return badgeResp;
        }).sorted(Comparator.comparing(BadgeResp::getWearing, Comparator.reverseOrder())
                .thenComparing(BadgeResp::getObtain, Comparator.reverseOrder())).toList();
    }
}
