package com.zuu.chatroom.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zuu.chatroom.user.domain.dto.ItemInfoDTO;
import com.zuu.chatroom.user.domain.dto.SummeryInfoDTO;
import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.user.domain.vo.req.BlackReq;
import com.zuu.chatroom.user.domain.vo.req.ItemInfoReq;
import com.zuu.chatroom.user.domain.vo.req.ModifyNameReq;
import com.zuu.chatroom.user.domain.vo.req.SummeryInfoReq;
import com.zuu.chatroom.user.domain.vo.resp.BadgeResp;
import com.zuu.chatroom.user.domain.vo.resp.UserInfoResp;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

import java.util.List;
import java.util.Map;

/**
* @author zuu
* @description 针对表【user(chat-room用户表)】的数据库操作Service
* @createDate 2024-07-06 09:57:19
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param openid
     */
    void register(String openid);

    /**
     * 生成token
     * @param id
     * @return
     */
    String login(Long id);
    /**
     * 校验token是不是有效
     *
     * @param token
     * @return
     */
    boolean verify(String token);

    /**
     * 刷新token有效期
     *
     * @param token
     */
    void renewalTokenIfNecessary(String token);

    Long getIdByToken(String token);

    /**
     * 用户授权后补全用户信息
     * @param userInfo
     * @return
     */
    User fillUserInfo(WxOAuth2UserInfo userInfo);

    /**
     * 获取用户详细信息
     * @param id
     * @return
     */
    UserInfoResp getUserInfo(Long id);

    /**
     * 修改用户名
     * @param id
     * @param modifyNameReq
     */
    void updateUserName(Long id, ModifyNameReq modifyNameReq);

    /**
     * 获取徽章列表
     * @param uid
     * @return
     */
    List<BadgeResp> getBadgeList(Long uid);

    void wearBadge(Long uid, Long itemId);

    /**
     * 拉黑用户
     * @param uid 发起请求的用户
     * @param req 目标用户
     */
    void black(Long uid, BlackReq req);

    Map<Long, Integer> getFriendActiveInfo(List<Long> friendIds);

    /**
     * 懒加载时前端请求后端用户信息
     */
    List<SummeryInfoDTO> getSummeryUserInfo(SummeryInfoReq req);

    /**
     * 懒加载时前端请求徽章信息
     */
    List<ItemInfoDTO> getItemInfo(ItemInfoReq req);

    Long getOnlineCount(List<Long> uids);
}
