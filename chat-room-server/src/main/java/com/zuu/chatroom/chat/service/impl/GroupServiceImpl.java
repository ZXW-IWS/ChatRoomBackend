package com.zuu.chatroom.chat.service.impl;

import com.zuu.chatroom.chat.domain.enums.GroupRoleEnum;
import com.zuu.chatroom.chat.domain.enums.RoomTypeEnum;
import com.zuu.chatroom.chat.domain.po.GroupMember;
import com.zuu.chatroom.chat.domain.po.Room;
import com.zuu.chatroom.chat.domain.po.RoomGroup;
import com.zuu.chatroom.chat.domain.vo.req.*;
import com.zuu.chatroom.chat.domain.vo.resp.AtMemberListResp;
import com.zuu.chatroom.chat.domain.vo.resp.ChatMemberResp;
import com.zuu.chatroom.chat.domain.vo.resp.MemberResp;
import com.zuu.chatroom.chat.service.*;
import com.zuu.chatroom.chat.service.adapter.GroupAdapter;
import com.zuu.chatroom.chat.service.adapter.MessageAdapter;
import com.zuu.chatroom.common.annotation.RedissonLock;
import com.zuu.chatroom.common.domain.dto.PushMsgDto;
import com.zuu.chatroom.common.domain.enums.YesOrNoEnum;
import com.zuu.chatroom.common.domain.vo.resp.CursorPageBaseResp;
import com.zuu.chatroom.common.exception.BusinessException;
import com.zuu.chatroom.common.service.MqService;
import com.zuu.chatroom.user.domain.enums.RoleEnum;
import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.user.service.RoleService;
import com.zuu.chatroom.user.service.UserService;
import com.zuu.chatroom.websocket.service.adapter.WebSocketAdapter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author zuu
 * @Description
 * @Date 2024/9/9 11:47
 */
@Service
@Slf4j
public class GroupServiceImpl implements GroupService {
    @Resource
    RoomService roomService;
    @Resource
    RoomFriendService roomFriendService;
    @Resource
    RoomGroupService roomGroupService;
    @Resource
    GroupMemberService groupMemberService;
    @Resource
    MqService mqService;
    @Resource
    UserService userService;
    @Resource
    RoleService roleService;
    @Resource
    ContactService contactService;
    @Resource
    ChatService chatService;
    @Resource
    @Lazy
    GroupServiceImpl groupServiceImpl;

    @Override
    public MemberResp getGroupDetail(Long uid, Long roomId) {
        RoomGroup roomGroup = roomGroupService.getByRoomId(roomId);
        if (Objects.isNull(roomGroup)) {
            throw new BusinessException("房间不存在！");
        }
        Room room = roomService.getById(roomId);
        //获取群聊的在线人数
        List<Long> groupMemberIds = groupMemberService.getGroupMemberList(roomGroup.getId());
        Long onlineCount = userService.getOnlineCount(groupMemberIds);
        //获取用户在群聊中的身份
        GroupMember groupMember = groupMemberService.getMember(uid, roomGroup.getId());
        Integer groupRole =
                (Objects.nonNull(groupMember))
                        ? groupRole = groupMember.getRole()
                        : GroupRoleEnum.REMOVED.getType();
        return GroupAdapter.buildGroupDetail(roomGroup, onlineCount, groupRole);
    }

    @Override
    public List<AtMemberListResp> getMemberList(AtMemberListReq atMemberListReq) {
        Long roomId = atMemberListReq.getRoomId();

        RoomGroup roomGroup = roomGroupService.getByRoomId(roomId);
        if (Objects.isNull(roomGroup)) {
            throw new BusinessException("房间" + roomId + "不存在!");
        }
        List<Long> groupMemberUidList = groupMemberService.getGroupMemberList(roomGroup.getId());
        List<User> userList = userService.listByIds(groupMemberUidList);

        return userList.stream().map(user -> {
            AtMemberListResp atMemberListResp = new AtMemberListResp();
            atMemberListResp.setUid(user.getId());
            atMemberListResp.setName(user.getNickname());
            atMemberListResp.setAvatar(user.getAvatar());

            return atMemberListResp;
        }).toList();
    }

    @Override
    @Transactional
    public void delMember(Long uid, MemberDelReq memberDelReq) {
        Long roomId = memberDelReq.getRoomId();
        Long deletedUid = memberDelReq.getUid();
        //1. 群聊是否存在
        RoomGroup roomGroup = roomGroupService.getByRoomId(roomId);
        if (Objects.isNull(roomGroup)) {
            throw new BusinessException("群聊" + roomId + "不存在");
        }
        //2. 当前操作的用户是否在该群聊中
        GroupMember self = groupMemberService.getMember(uid, roomGroup.getId());
        if (Objects.isNull(self)) {
            throw new BusinessException("您没有权限");
        }
        //3. 被踢的用户是否在该群聊中
        GroupMember deleted = groupMemberService.getMember(deletedUid, roomGroup.getId());
        if (Objects.isNull(deleted)) {
            throw new BusinessException("该用户已被移出");
        }
        //4. 群主不可被移出
        if (Objects.equals(deleted.getRole(), GroupRoleEnum.LEADER.getType())) {
            throw new BusinessException("群主不可被移出！");
        }
        //5. 管理员只有群主可以移出
        if (Objects.equals(deleted.getRole(), GroupRoleEnum.MANAGER.getType())
                && !Objects.equals(self.getRole(), GroupRoleEnum.LEADER.getType())) {
            throw new BusinessException("只有群主才能移出管理员！");
        }
        //6. 只有管理员或者群主才能操作
        boolean hasPower = roleService.hasPower(uid, RoleEnum.SUPER_ADMIN)
                || Objects.equals(self.getRole(), GroupRoleEnum.LEADER.getType())
                || Objects.equals(self.getRole(), GroupRoleEnum.MANAGER.getType());
        if (!hasPower) {
            throw new BusinessException("您没有权限");
        }
        groupMemberService.delMember(deletedUid, roomGroup.getId());
        //将群成员移出消息发送给群聊中的其他用户
        List<Long> groupMemberList = groupMemberService.getGroupMemberList(roomGroup.getId());
        mqService.sendPushMsg(new PushMsgDto(groupMemberList, WebSocketAdapter.buildMemberRemovedResp(roomId, deletedUid)));
    }

    @Override
    @Transactional
    public void exitGroup(Long uid, MemberExitReq memberExitReq) {
        Long roomId = memberExitReq.getRoomId();
        //1. 群聊是否存在
        RoomGroup roomGroup = roomGroupService.getByRoomId(roomId);
        if (Objects.isNull(roomGroup)) {
            throw new BusinessException("群聊" + roomId + "不存在");
        }
        //2. hot标记的群聊不允许退出
        Room room = roomService.getById(roomId);
        if (Objects.equals(room.getHotFlag(), YesOrNoEnum.YES.getStatus())) {
            throw new BusinessException("该群聊不允许退出");
        }
        //3. 用户是否在该群聊中
        GroupMember groupMember = groupMemberService.getMember(uid, roomGroup.getId());
        if (Objects.isNull(groupMember)) {
            throw new BusinessException("您已经退出群聊");
        }
        //4. 群主退出群聊特殊处理
        if (Objects.equals(groupMember.getRole(), GroupRoleEnum.LEADER.getType())) {
            //4.1. 若群聊中有管理员，那么就将群主转让给加入最早的一位管理员
            GroupMember nextLeader = groupMemberService.getFirstManager(roomGroup.getId());
            //4.2. 若群聊中没有管理员，就将群主转让给最早加入的用户
            if (Objects.isNull(nextLeader)) {
                nextLeader = groupMemberService.getFirstMember(roomGroup.getId());
            }
            //4.3. 若没有用户，则下面直接解散群聊
            if (Objects.nonNull(nextLeader)) {
                groupMemberService.changeRole(nextLeader.getId(), GroupRoleEnum.LEADER);
            }
        }
        //5. 退出群聊通用逻辑处理
        //5.1. 删除对应的会话
        contactService.removeContact(uid, roomId);
        //5.2. 删除群成员表中的对应行
        groupMemberService.delMember(uid, roomGroup.getId());
        //5.3. 若群聊用户只剩下一人，那么就直接解散群聊
        Boolean isDel = roomService.delGroupIfNeed(roomGroup.getRoomId(), roomGroup.getId());
        if (!isDel) {
            //群聊未解散，则将用户退出群聊信息告知其他群成员
            List<Long> groupMemberList = groupMemberService.getGroupMemberList(roomGroup.getId());
            mqService.sendPushMsg(new PushMsgDto(groupMemberList, WebSocketAdapter.buildMemberRemovedResp(roomId, uid)));
        }
    }

    @Override
    @Transactional
    public Long addGroup(Long uid, GroupAddReq groupAddReq) {
        List<User> userList = userService.listByIds(groupAddReq.getUidList());
        //1. 创建对应的Room表信息
        Room room = roomService.createRoom(RoomTypeEnum.GROUP.getType());
        //2. 创建对应的RoomGroup表信息
        User user = userService.getById(uid);
        RoomGroup roomGroup = roomGroupService.createGroup(user, room.getId(), userList);
        //3. 插入群主信息到GroupMember表
        groupMemberService.addMember(uid, roomGroup.getId(), GroupRoleEnum.LEADER);
        //4. 为uidList都创建GroupMember表项
        groupMemberService.addMemberBatch(groupAddReq.getUidList(), roomGroup.getId(), GroupRoleEnum.MEMBER);
        noticeGroupMember(user, groupAddReq.getUidList(), userList, room);
        return room.getId();
    }

    private void noticeGroupMember(User user, List<Long> noticeUidList, List<User> invitedUserList, Room room) {
        //5. 通过sendMsg方法向群聊中发送消息（XX邀请了XX,XX,XX...加入群聊）
        chatService.sendMsg(user.getId(), MessageAdapter.buildGroupAddMsg(user, invitedUserList, room.getId()));
        //6. mq向用户发送群成员变动的消息（用于触发每个用户的会话创建）
        //TODO: 测试后再判断这个地方不发送系统消息是否有bug
        invitedUserList.forEach(invitedUser -> {
            mqService.sendPushMsg(new PushMsgDto(noticeUidList, WebSocketAdapter.buildGroupAddAResp(invitedUser,room.getId())));
        });

    }

    @Override
    @Transactional
    @RedissonLock(key = "#memberAddReq.roomId")
    public void addMember(Long uid, MemberAddReq memberAddReq) {
        Long roomId = memberAddReq.getRoomId();
        List<Long> uidList = memberAddReq.getUidList();
        //1. 群聊是否存在
        Room room = roomService.getById(roomId);
        RoomGroup roomGroup = roomGroupService.getByRoomId(roomId);
        if (Objects.isNull(roomGroup)) {
            throw new BusinessException("群聊" + roomId + "不存在");
        }
        //2. hot标记的群聊只有超管允许邀请用户
        if (Objects.equals(room.getHotFlag(), YesOrNoEnum.YES.getStatus()) && !roleService.hasPower(uid, RoleEnum.SUPER_ADMIN)) {
            throw new BusinessException("您无权邀请用户");
        }
        //3. 用户是否是该群成员
        GroupMember self = groupMemberService.getMember(uid, roomGroup.getId());
        if (Objects.isNull(self)) {
            throw new BusinessException("您还不是群成员");
        }
        //4. 获取群聊的用户uid列表并转为Set
        List<Long> groupMemberList = groupMemberService.getGroupMemberList(roomGroup.getId());
        Set<Long> groupMemberSet = new HashSet<>(groupMemberList);
        //5. 遍历uidList,若不在群聊中则创建对应的GroupMember表项
        List<Long> needAddUidList = uidList.stream().filter(id -> !groupMemberSet.contains(id)).toList();
        groupMemberService.addMemberBatch(needAddUidList, roomGroup.getId(), GroupRoleEnum.MEMBER);
        //6. 通过sendMsg方法向群聊中发送消息（XX邀请了XX,XX,XX...加入群聊）（和创建群聊一致，抽象为公共方法）
        List<User> needAddUserList = userService.listByIds(needAddUidList);
        User user = userService.getById(uid);
        noticeGroupMember(user, groupMemberList, needAddUserList, room);
    }

    @Override
    @Transactional
    public void addAdmin(Long uid, AdminAddReq adminAddReq) {
        Long roomId = adminAddReq.getRoomId();
        List<Long> uidList = adminAddReq.getUidList();
        groupServiceImpl.checkAndChangeUserRole(uid, roomId, uidList, GroupRoleEnum.MANAGER);
    }

    @Override
    @Transactional
    public void revokeAdmin(Long uid, AdminRevokeReq adminRevokeReq) {
        Long roomId = adminRevokeReq.getRoomId();
        List<Long> uidList = adminRevokeReq.getUidList();
        groupServiceImpl.checkAndChangeUserRole(uid, roomId, uidList, GroupRoleEnum.MEMBER);
    }

    @Override
    public CursorPageBaseResp<ChatMemberResp> getMemberPage(MemberReq memberReq) {
        Long roomId = memberReq.getRoomId();
        //1. 校验房间是否存在
        RoomGroup roomGroup = roomGroupService.getByRoomId(roomId);
        if(Objects.isNull(roomGroup)){
            throw new BusinessException("群聊" + roomId +"不存在");
        }
        //2. 获取房间的所有用户信息
        List<Long> groupMemberList = groupMemberService.getGroupMemberList(roomGroup.getId());
        List<User> userList = userService.listByIds(groupMemberList);
        Map<Long,Integer> userRoleMap = groupMemberService.getMemberRoleBatch(roomGroup.getId(),groupMemberList);
        List<ChatMemberResp> chatMemberRespList = userList.stream().map(user -> {
            ChatMemberResp chatMemberResp = new ChatMemberResp();
            chatMemberResp.setUid(user.getId());
            chatMemberResp.setActiveStatus(user.getActiveStatus());
            chatMemberResp.setRoleId(userRoleMap.get(user.getId()));
            chatMemberResp.setLastOptTime(user.getLastLoginTime());

            return chatMemberResp;
        }).toList();
        //3. 按照用户是否在线进行排序,并根据上下线时间分别进行排序（前端已经做了，后端直接返回用户列表）
        return CursorPageBaseResp.all(chatMemberRespList);
    }

    @Transactional
    public void checkAndChangeUserRole(Long uid, Long roomId, List<Long> uidList, GroupRoleEnum groupRoleEnum) {
        //1. 群聊是否存在
        RoomGroup roomGroup = roomGroupService.getByRoomId(roomId);
        if (Objects.isNull(roomGroup)) {
            throw new BusinessException("房间" + roomId + "不存在");
        }
        //2. 用户是否是群主
        GroupMember self = groupMemberService.getMember(uid, roomGroup.getId());
        if (!Objects.equals(self.getRole(), GroupRoleEnum.LEADER.getType())) {
            throw new BusinessException("您无权添加管理员");
        }
        //3. 目标用户是否在群聊中
        List<Long> groupMemberList = groupMemberService.getGroupMemberList(roomGroup.getId());
        HashSet<Long> groupMemberSet = new HashSet<>(groupMemberList);
        boolean containsAll = groupMemberSet.containsAll(uidList);
        if (!containsAll) {
            throw new BusinessException("目标用户不在群聊内");
        }
        //4. 修改GroupMember表中用户的身份
        groupMemberService.changeRoleBatch(roomGroup.getId(), uidList, groupRoleEnum);
    }
}
