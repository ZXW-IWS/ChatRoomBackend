package com.zuu.chatroom.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuu.chatroom.common.domain.vo.req.CursorPageBaseReq;
import com.zuu.chatroom.common.domain.vo.resp.CursorPageBaseResp;
import com.zuu.chatroom.user.domain.po.UserFriend;
import com.zuu.chatroom.user.mapper.UserFriendMapper;
import com.zuu.chatroom.user.service.UserFriendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author zuu
* @description 针对表【user_friend(用户联系人表)】的数据库操作Service实现
* @createDate 2024-07-18 09:30:26
*/
@Service
public class UserFriendServiceImpl extends ServiceImpl<UserFriendMapper, UserFriend>
    implements UserFriendService {

    /**
     * 游标翻页查询用户的好友
     * @param uid 用户id
     * @param getFriendReq 游标翻页信息
     * @return
     */
    @Override
    public CursorPageBaseResp<UserFriend> getFriendPage(Long uid, CursorPageBaseReq getFriendReq) {

        Integer pageSize = getFriendReq.getPageSize();
        String cursor = getFriendReq.getCursor();
        //组装查询wrapper
        QueryWrapper<UserFriend> wrapper = new QueryWrapper<>();
        wrapper.eq("uid",uid);
        if(StrUtil.isNotBlank(cursor)){
            wrapper.lt("id",getFriendReq.getCursor());
        }
        return null;
    }

    @Override
    public List<UserFriend> getFriendList(Long uid) {
        return this.list(new QueryWrapper<UserFriend>().eq("uid", uid));
    }

    @Override
    public Set<Long> getFriendInIds(Long uid, List<Long> uidList) {
        List<UserFriend> friendList = this.list(new QueryWrapper<UserFriend>().eq("uid", uid).in("friend_uid", uidList));
        return friendList.stream().map(UserFriend::getFriendUid).collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void createFriend(Long uid, Long friendId) {
        UserFriend userFriend1 = new UserFriend();
        userFriend1.setUid(uid);
        userFriend1.setFriendUid(friendId);

        UserFriend userFriend2 = new UserFriend();
        userFriend2.setUid(friendId);
        userFriend2.setFriendUid(uid);

        this.saveBatch(Arrays.asList(userFriend1,userFriend2));
    }

    @Override
    public Long getFriendCount(Long uid) {
        return this.count(new QueryWrapper<UserFriend>().eq("uid", uid));
    }

    /**
     * 获取两个用户的好友关系
     */
    @Override
    public List<UserFriend> getUserFriend(Long uid, Long targetUid) {
        return this.list(
                new QueryWrapper<UserFriend>()
                        .eq("uid", uid)
                        .eq("friend_uid", targetUid)
                        .or()
                        .eq("uid", targetUid)
                        .eq("friend_uid", uid));
    }
}




