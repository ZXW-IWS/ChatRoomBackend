package com.zuu.chatroom.user.service.impl;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuu.chatroom.common.domain.enums.YesOrNoEnum;
import com.zuu.chatroom.common.exception.BusinessException;
import com.zuu.chatroom.common.service.MqService;
import com.zuu.chatroom.common.utils.RedisUtils;
import com.zuu.chatroom.user.domain.dto.ItemInfoDTO;
import com.zuu.chatroom.user.domain.dto.SummeryInfoDTO;
import com.zuu.chatroom.user.domain.enums.*;
import com.zuu.chatroom.user.domain.po.Black;
import com.zuu.chatroom.user.domain.po.Item;
import com.zuu.chatroom.user.domain.po.ItemPackage;
import com.zuu.chatroom.user.domain.vo.req.BlackReq;
import com.zuu.chatroom.user.domain.vo.req.ItemInfoReq;
import com.zuu.chatroom.user.domain.vo.req.ModifyNameReq;
import com.zuu.chatroom.user.domain.vo.req.SummeryInfoReq;
import com.zuu.chatroom.user.domain.vo.resp.BadgeResp;
import com.zuu.chatroom.user.domain.vo.resp.UserInfoResp;
import com.zuu.chatroom.user.mapper.UserMapper;
import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.user.service.*;
import com.zuu.chatroom.user.service.adapter.UserAdapter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.zuu.chatroom.common.constant.RedisConstant.USER_TOKEN_KEY;
import static com.zuu.chatroom.common.constant.RedisConstant.USER_TOKEN_TTL_HOURS;

/**
 * @author zuu
 * @description 针对表【user(chat-room用户表)】的数据库操作Service实现
 * @createDate 2024-07-06 09:57:19
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private ItemPackageService itemPackageService;
    @Resource
    private ItemService itemService;

    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private MqService mqService;
    @Resource
    private RoleService roleService;
    @Resource
    private BlackService blackService;

    @Override
    @Transactional
    public void register(String openid) {
        User user = new User();
        user.setOpenid(openid);
        boolean saved = this.save(user);

        //发送用户成功注册消息
        mqService.sendUserRegisterMsg(user);

    }


    @Override
    public String login(Long id) {
        String token = IdUtil.simpleUUID();
        String key = USER_TOKEN_KEY + token;
        //token保存到redis
        RedisUtils.set(key, id, USER_TOKEN_TTL_HOURS, TimeUnit.HOURS);

        return token;
    }

    /**
     * 校验token是不是有效
     *
     * @param token
     * @return
     */
    @Override
    public boolean verify(String token) {
        String key = USER_TOKEN_KEY + token;
        Long id = RedisUtils.get(key, Long.class);

        return !ObjectUtil.isNull(id);
    }

    @Override
    public void renewalTokenIfNecessary(String token) {
        if (!verify(token))
            return;
        String key = USER_TOKEN_KEY + token;
        //刷新token有效事件
        RedisUtils.expire(key, USER_TOKEN_TTL_HOURS, TimeUnit.HOURS);
    }

    @Override
    public Long getIdByToken(String token) {
        String key = USER_TOKEN_KEY + token;

        return RedisUtils.get(key, Long.class);
    }

    @Override
    @Transactional
    public User fillUserInfo(WxOAuth2UserInfo userInfo) {
        User user = this.getOne(new QueryWrapper<User>().eq("openid", userInfo.getOpenid()));
        if (ObjectUtil.isNull(user)) {
            throw new BusinessException("用户不存在");
        }
        //若用户头像昵称不为空，就不需要更新用户信息
        if (StrUtil.isAllNotBlank(user.getNickname(), user.getAvatar())) {
            return user;
        }
        //否则就需要更新
        user.setNickname(userInfo.getNickname());
        user.setAvatar(userInfo.getHeadImgUrl());
        user.setSex(userInfo.getSex());
        this.updateById(user);
        return user;
    }

    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    @Override
    public UserInfoResp getUserInfo(Long id) {
        User user = this.getById(id);
        int modifyNameChance = itemPackageService.getCountByItemId(id, ItemEnum.MODIFY_NAME_CARD.getId());
        return UserAdapter.buildUserInfoResp(user, modifyNameChance);
    }

    /**
     * 修改用户名
     *
     * @param id            用户id
     * @param modifyNameReq
     */
    @Override
    @Transactional
    public void updateUserName(Long id, ModifyNameReq modifyNameReq) {
        //1. 判断用户是否有改名卡
        ItemPackage modifyItem = itemPackageService.getFirstItem(id, ItemEnum.MODIFY_NAME_CARD.getId());
        if (ObjectUtil.isNull(modifyItem))
            throw new BusinessException("您还没有改名卡");
        //2.使用改名卡并修改用户名
        boolean useSuccess = itemPackageService.useItem(modifyItem.getId());
        if (useSuccess) {
            //改名
            this.update(new UpdateWrapper<User>()
                    .eq("id", id)
                    .set("nickname", modifyNameReq.getNickname()));
        }
    }

    @Override
    public List<BadgeResp> getBadgeList(Long uid) {
        //1.获取徽章列表
        List<Item> badgeList = itemService.getListByType(ItemTypeEnum.BADGE.getType());
        //2.获取用户拥有的徽章列表
        List<Long> badgeIds = badgeList.stream().map(Item::getId).toList();
        List<ItemPackage> packages = itemPackageService.getByUid(uid, badgeIds);
        //获取用户佩戴的徽章
        User user = this.getById(uid);
        return UserAdapter.buildBadgeListResp(badgeList, packages, user);
    }

    @Override
    @Transactional
    public void wearBadge(Long uid, Long itemId) {
        //1. 确保该物品是徽章
        Item item = itemService.getById(itemId);
        if (Objects.isNull(item) || !ItemTypeEnum.BADGE.getType().equals(item.getType())) {
            throw new BusinessException("物品不存在或物品不是徽章");
        }
        //2. 确保用户拥有这个徽章
        ItemPackage firstItem = itemPackageService.getFirstItem(uid, itemId);
        if (Objects.isNull(firstItem)) {
            throw new BusinessException("您还没有这个徽章哦");
        }
        //3. 若用户当前已佩戴该徽章，就不必再佩戴了
        User user = this.getById(uid);
        if (itemId.equals(user.getItemId()))
            return;
        //4. 用户佩戴徽章
        this.update(new UpdateWrapper<User>().eq("id", uid).set("item_id", itemId));
    }

    /**
     * @param uid 发起请求的用户
     * @param req 目标用户
     */
    @Override
    public void black(Long uid, BlackReq req) {
        boolean hasPower = roleService.hasPower(uid, RoleEnum.SUPER_ADMIN);
        if (!hasPower) {
            throw new BusinessException("没有管理员权限");
        }
        Long targetUid = req.getUid();
        //拉黑用户id
        blackUid(targetUid);

        //拉黑用户ip
        User targetUser = this.getById(targetUid);
        String createIp = targetUser.getIpInfo().getCreateIp();
        String updateIp = targetUser.getIpInfo().getUpdateIp();
        blackIp(createIp);
        blackIp(updateIp);

        //发送拉黑事件
        mqService.sendBlackUserMsg(targetUser);
    }

    /**
     * 返回好友的在线状态信息
     *
     * @param friendIds
     * @return
     */
    @Override
    public Map<Long, Integer> getFriendActiveInfo(List<Long> friendIds) {
        List<User> friends = this.list(new QueryWrapper<User>().in("id", friendIds));
        Map<Long, Integer> friendActiveMap = new HashMap<>();
        friends.forEach(friend -> {
            friendActiveMap.put(friend.getId(), friend.getActiveStatus());
        });
        return friendActiveMap;
    }

    @Override
    public List<SummeryInfoDTO> getSummeryUserInfo(SummeryInfoReq req) {
        List<SummeryInfoReq.infoReq> reqList = req.getReqList();
        //1.获取需要更新用户信息
        Set<Long> needRefreshIds = getNeedRefreshUsers(req);
        Map<Long, User> needRefreshUserMap = new HashMap<>();
        if(!needRefreshIds.isEmpty()){
            this.listByIds(needRefreshIds).forEach(user -> {
                needRefreshUserMap.put(user.getId(), user);
            });
        }
        //2.判断是否需要更新向用户返回相应的信息
        List<SummeryInfoDTO> res = new ArrayList<>(reqList.size());
        List<Long> uidList = reqList.stream().map(SummeryInfoReq.infoReq::getUid).toList();
        //3.组装返回信息
        uidList.forEach(uid -> {
            if (needRefreshIds.contains(uid))
                res.add(needRefreshDto(needRefreshUserMap.get(uid)));
            else
                res.add(noNeedRefreshDto(uid));
        });

        return res;
    }

    @Override
    public List<ItemInfoDTO> getItemInfo(ItemInfoReq req) {
        return req.getReqList().stream().map(infoReq -> {
            Item item = itemService.getById(infoReq.getItemId());
            if (Objects.isNull(infoReq.getLastModifyTime()) || item.getUpdateTime().getTime() > infoReq.getLastModifyTime()) {
                ItemInfoDTO itemInfoDTO = new ItemInfoDTO();
                itemInfoDTO.setItemId(item.getId());
                itemInfoDTO.setNeedRefresh(Boolean.FALSE);
                return itemInfoDTO;
            }
            ItemInfoDTO itemInfoDTO = new ItemInfoDTO();
            itemInfoDTO.setItemId(item.getId());
            itemInfoDTO.setNeedRefresh(Boolean.TRUE);
            itemInfoDTO.setImg(item.getImg());
            itemInfoDTO.setDescribe(item.getDescribe());
            return itemInfoDTO;
        }).toList();
    }

    @Override
    public Long getOnlineCount(List<Long> uids) {
        return this.count(
                new QueryWrapper<User>()
                        .eq("active_status", UserActiveStatusEnum.ONLINE.getType())
                        .in("id", uids));
    }

    private SummeryInfoDTO needRefreshDto(User user) {
        SummeryInfoDTO summeryInfoDTO = new SummeryInfoDTO();
        summeryInfoDTO.setUid(user.getId());
        summeryInfoDTO.setNeedRefresh(Boolean.TRUE);
        summeryInfoDTO.setName(user.getNickname());
        summeryInfoDTO.setAvatar(user.getAvatar());
        summeryInfoDTO.setLocPlace(user.getIpInfo().getUpdateIpDetail().getCity());
        summeryInfoDTO.setWearingItemId(user.getItemId());

        //获取用户徽章
        List<Item> badgeList = itemService.getListByType(ItemTypeEnum.BADGE.getType());
        List<ItemPackage> userPackage =
                itemPackageService.getByUid(user.getId(), badgeList.stream().map(Item::getId).toList());
        List<Long> itemIds = userPackage.stream().map(ItemPackage::getItemId).toList();
        summeryInfoDTO.setItemIds(itemIds);

        return summeryInfoDTO;
    }

    private SummeryInfoDTO noNeedRefreshDto(Long uid) {
        SummeryInfoDTO summeryInfoDTO = new SummeryInfoDTO();
        summeryInfoDTO.setUid(uid);
        summeryInfoDTO.setNeedRefresh(Boolean.FALSE);
        return summeryInfoDTO;
    }

    /**
     * 获取请求中需要更新的uid的set
     */
    private Set<Long> getNeedRefreshUsers(SummeryInfoReq req) {
        List<Long> uidList = req.getReqList().stream().map(SummeryInfoReq.infoReq::getUid).toList();
        List<User> userList = this.listByIds(uidList);
        Map<Long, Long> lastModifyMap = new HashMap<>(req.getReqList().size());
        req.getReqList().forEach(infoReq -> {
            lastModifyMap.put(infoReq.getUid(), infoReq.getLastModifyTime());
        });
        Set<Long> res = new HashSet<>();
        userList.forEach(user -> {
            long lastUpdateTime = user.getUpdateTime().getTime();
            Long lastUpdateTimeFromReq = lastModifyMap.get(user.getId());
            //判断是否需要更新
            if (Objects.isNull(lastUpdateTimeFromReq) || lastUpdateTime > lastUpdateTimeFromReq) {
                res.add(user.getId());
            }
        });

        return res;
    }

    private void blackIp(String ip) {
        if (StrUtil.isBlank(ip))
            return;
        try {
            Black black = new Black();
            black.setType(BlackTypeEnum.IP.getType());
            black.setTarget(ip);
            blackService.save(black);
            log.info("black ip success,the ip is:[{}]", ip);
        } catch (Exception e) {
            log.error("duplicate black ip,the ip is:[{}]", ip);
        }

    }

    private void blackUid(Long targetUid) {
        Black black = new Black();
        black.setType(BlackTypeEnum.UID.getType());
        black.setTarget(targetUid.toString());
        blackService.save(black);
        log.info("black user success,the user's id is:[{}]", targetUid);
    }
}




