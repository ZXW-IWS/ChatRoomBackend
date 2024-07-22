package com.zuu.chatroom.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zuu.chatroom.common.domain.vo.req.CursorPageBaseReq;
import com.zuu.chatroom.common.domain.vo.resp.CursorPageBaseResp;
import com.zuu.chatroom.user.domain.po.UserFriend;

import java.util.List;
import java.util.Set;

/**
* @author zuu
* @description 针对表【user_friend(用户联系人表)】的数据库操作Service
* @createDate 2024-07-18 09:30:26
*/
public interface UserFriendService extends IService<UserFriend> {

    /**
     * 游标翻页查询用户的好友
     * @param uid 用户id
     * @param getFriendReq 游标翻页信息
     * @return
     */
    CursorPageBaseResp<UserFriend> getFriendPage(Long uid, CursorPageBaseReq getFriendReq);

    /**
     * 查询用户的好友列表
     * @param uid
     * @return
     */
    List<UserFriend> getFriendList(Long uid);

    Set<Long> getFriendInIds(Long uid, List<Long> uidList);

    void createFriend(Long uid, Long friendId);

    Long getFriendCount(Long uid);

    /**
     * 获取两个用户的好友关系
     */
    List<UserFriend> getUserFriend(Long uid, Long targetUid);
}
