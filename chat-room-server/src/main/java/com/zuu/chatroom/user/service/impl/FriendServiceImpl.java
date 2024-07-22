package com.zuu.chatroom.user.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zuu.chatroom.common.annotation.RedissonLock;
import com.zuu.chatroom.common.domain.vo.req.CursorPageBaseReq;
import com.zuu.chatroom.common.domain.vo.req.PageBaseReq;
import com.zuu.chatroom.common.domain.vo.resp.PageBaseResp;
import com.zuu.chatroom.common.exception.BusinessException;
import com.zuu.chatroom.user.domain.enums.ApplyReadStatusEnum;
import com.zuu.chatroom.user.domain.enums.ApplyStatusEnum;
import com.zuu.chatroom.user.domain.po.UserApply;
import com.zuu.chatroom.user.domain.po.UserFriend;
import com.zuu.chatroom.user.domain.vo.req.FriendApplyReq;
import com.zuu.chatroom.user.domain.vo.req.FriendApproveReq;
import com.zuu.chatroom.user.domain.vo.req.FriendCheckReq;
import com.zuu.chatroom.user.domain.vo.req.FriendListReq;
import com.zuu.chatroom.user.domain.vo.resp.FriendApplyResp;
import com.zuu.chatroom.user.domain.vo.resp.FriendCheckResp;
import com.zuu.chatroom.user.domain.vo.resp.FriendResp;
import com.zuu.chatroom.user.domain.vo.resp.FriendUnreadResp;
import com.zuu.chatroom.user.service.*;
import com.zuu.chatroom.user.service.adapter.FriendAdapter;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.zuu.chatroom.common.constant.RedisConstant.BASE_KEY;
import static com.zuu.chatroom.common.constant.UserConstant.MAX_FRIEND_COUNT;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/18 11:01
 */
@Service
public class FriendServiceImpl implements FriendService {
    @Resource
    UserFriendService userFriendService;
    @Resource
    UserApplyService userApplyService;
    @Resource
    UserService userService;
    @Resource
    MqService mqService;
    @Override
    public List<FriendResp> friendList(Long uid) {
        //1. 获取用户的好友列表
        List<UserFriend> friendList = userFriendService.getFriendList(uid);
        //2. 获取好友是否在线
        List<Long> friendIds = friendList.stream().map(UserFriend::getFriendUid).toList();
        Map<Long,Integer> friendActiveMap = userService.getFriendActiveInfo(friendIds);
        //3. 组装结果并按照用户在线状态排序
        List<FriendResp> friendRespList = friendIds.stream().map(friendId -> {
            FriendResp friendResp = new FriendResp();
            friendResp.setUid(friendId);
            friendResp.setActiveStatus(friendActiveMap.get(friendId));
            return friendResp;
        }).sorted(Comparator.comparingInt(FriendResp::getActiveStatus)).toList();

        return friendRespList;
    }

    /**
     * 分页获取用户的好友申请列表
     */
    @Override
    public PageBaseResp<FriendApplyResp> pageApplyFriend(Long uid, PageBaseReq getApplyListReq) {
        //1. 获取用户的好友申请分页数据
        Page<UserApply> applyPage = userApplyService.getApplyList(uid, getApplyListReq);
        //2. 若没有好友申请记录，返回空的信息
        if(Objects.isNull(applyPage.getRecords()) || applyPage.getRecords().isEmpty())
            return PageBaseResp.empty();
        //3. 将这些记录修改为已读
        List<Long> applyIds = applyPage.getRecords().stream().map(UserApply::getId).toList();
        userApplyService.readApples(applyIds);

        return PageBaseResp.init(applyPage, FriendAdapter.buildPageApplyResp(applyPage.getRecords()));
    }

    @Override
    public FriendCheckResp check(Long uid, FriendCheckReq friendCheckReq) {
        //1. 获取list中好友id的set
        Set<Long> friendIdSet = userFriendService.getFriendInIds(uid,friendCheckReq.getUidList());
        //2.组装结果
        List<FriendCheckResp.FriendCheck> friendCheckResp = friendCheckReq.getUidList().stream().map(friendId -> {
            FriendCheckResp.FriendCheck friendCheck = new FriendCheckResp.FriendCheck();
            friendCheck.setUid(friendId);
            friendCheck.setIsFriend(friendIdSet.contains(friendId));
            return friendCheck;
        }).toList();
        return new FriendCheckResp(friendCheckResp);
    }

    @Override
    public FriendUnreadResp unread(Long uid) {
        Long unReadCount = userApplyService.getUnReadCount(uid);
        return new FriendUnreadResp(unReadCount);
    }

    @Override
    @Transactional
    @RedissonLock(key = "#uid")
    public void applyApprove(Long uid, FriendApproveReq friendApproveReq) {
        Long applyId = friendApproveReq.getApplyId();
        UserApply userApply = userApplyService.getById(applyId);
        if(Objects.isNull(userApply))
            throw new BusinessException("该申请记录不存在");
        if(!uid.equals(userApply.getTargetId()))
            throw new BusinessException("该申请记录不存在");
        if(!userApply.getStatus().equals(ApplyStatusEnum.WAIT_APPROVE.getStatus()))
            throw new BusinessException("该申请记录已经同意过了");
        //确定每个用户只能添加100个用户
        Long friendCount = userFriendService.getFriendCount(uid);
        if(friendCount >= MAX_FRIEND_COUNT)
            throw new BusinessException("您只能添加最多" + MAX_FRIEND_COUNT + "个好友!");
        //修改记录为同意
        userApplyService.agree(applyId);
        //创建好友关系
        userFriendService.createFriend(uid,userApply.getUid());

        //TODO:为两个用户创建一个新的房间并发送打招呼的消息
    }

    @Override
    @Transactional
    @RedissonLock(key = "#uid")
    public void deleteFriend(Long uid, Long targetUid) {
        List<UserFriend> userFriendList = userFriendService.getUserFriend(uid,targetUid);
        if(Objects.isNull(userFriendList) || userFriendList.isEmpty())
            throw new BusinessException("你们不是好友哦~");
        List<Long> userFriendIds = userFriendList.stream().map(UserFriend::getId).toList();
        userFriendService.removeBatchByIds(userFriendIds);
        //TODO:禁用房间
    }

    @Override
    @Transactional
    @RedissonLock(key = "#uid")
    public void applyFriend(Long uid, FriendApplyReq friendApplyReq) {
        Long targetUid = friendApplyReq.getTargetUid();
        String applyReqMsg = friendApplyReq.getMsg();
        //1.是否已经是好友了
        List<UserFriend> userFriend = userFriendService.getUserFriend(uid, targetUid);
        if(Objects.nonNull(userFriend) && !userFriend.isEmpty()){
            throw new BusinessException("你们已经是好友啦");
        }
        //2.好友数是否已到上限
        Long friendCount = userFriendService.getFriendCount(uid);
        if(friendCount >= MAX_FRIEND_COUNT)
            throw new BusinessException("您只能添加最多" + MAX_FRIEND_COUNT + "个好友!");
        //3.是否已经申请过好友了
        UserApply userApply = userApplyService.getApply(uid,targetUid);
        if(Objects.nonNull(userApply))
            throw new BusinessException("您已经发送过好友申请了");
        //4.对方是否正在申请自己为好友
        userApply = userApplyService.getApply(targetUid,uid);
        if(Objects.nonNull(userApply))
            throw new BusinessException("对方已经向您发送过好友申请了");
        //5.保存申请记录
        Long applyId = userApplyService.saveApply(uid,targetUid,applyReqMsg);
        //6.发送申请事件
        mqService.sendApplyMsg(applyId);
    }

}
