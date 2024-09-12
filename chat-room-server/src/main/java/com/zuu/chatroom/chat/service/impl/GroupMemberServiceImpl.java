package com.zuu.chatroom.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuu.chatroom.chat.domain.enums.GroupRoleEnum;
import com.zuu.chatroom.chat.domain.po.GroupMember;
import com.zuu.chatroom.chat.mapper.GroupMemberMapper;
import com.zuu.chatroom.chat.service.GroupMemberService;
import com.zuu.chatroom.chat.service.adapter.RoomAdapter;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.zuu.chatroom.common.constant.RedisConstant.GROUP_MEMBER_LIST_MINUTES;
import static com.zuu.chatroom.common.constant.RedisConstant.GROUP_MEMBER_LIST_PREFIX;

/**
 * @author zuu
 * @description 针对表【group_member(群成员表)】的数据库操作Service实现
 * @createDate 2024-07-22 14:15:04
 */
@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember>
        implements GroupMemberService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public GroupMember getMember(Long uid, Long groupId) {
        return this.getOne(new QueryWrapper<GroupMember>().eq("group_id", groupId).eq("uid", uid));
    }

    @Override
    public List<Long> getGroupMemberList(Long groupId) {
        //每次访问成员列表时，都刷新一次有效时间；若成员列表有变动，直接删除缓存信息；若访问时对应key没有数据，就查询数据库并更新redis。
        String key = GROUP_MEMBER_LIST_PREFIX + groupId;
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            //redis中存在
            List<Long> memberUidList = Objects.requireNonNull(stringRedisTemplate.opsForList().range(key, 0, -1))
                    .stream().map(Long::parseLong).toList();
            //刷新有效时间
            stringRedisTemplate.expire(key, GROUP_MEMBER_LIST_MINUTES, TimeUnit.MINUTES);
            return memberUidList;
        } else {
            //redis中不存在
            List<GroupMember> groupMembers = this.list(new QueryWrapper<GroupMember>().eq("group_id", groupId));
            List<Long> groupMemberUidList = groupMembers.stream().map(GroupMember::getUid).toList();
            //存入redis中
            if(!groupMemberUidList.isEmpty() && Boolean.FALSE.equals(stringRedisTemplate.hasKey(key))){
                stringRedisTemplate.opsForList().rightPushAll(key, groupMemberUidList.stream().map(Objects::toString).toList());
                stringRedisTemplate.expire(key, GROUP_MEMBER_LIST_MINUTES, TimeUnit.MINUTES);
            }
            return groupMemberUidList;
        }
    }

    @Override
    @Transactional
    public void delMember(Long deletedUid, Long groupId) {
        //删除数据库表项
        this.remove(new QueryWrapper<GroupMember>().eq("group_id", groupId).eq("uid", deletedUid));
        //清除缓存
        clearGroupRedis(groupId);
    }

    @Override
    public GroupMember getFirstManager(Long groupId) {
        return this.getOne(new QueryWrapper<GroupMember>()
                .eq("group_id", groupId)
                .eq("role", GroupRoleEnum.MANAGER.getType()));
    }

    @Override
    public GroupMember getFirstMember(Long groupId) {
        return this.getOne(new QueryWrapper<GroupMember>()
                .eq("group_id", groupId)
                .eq("role", GroupRoleEnum.MEMBER.getType()));
    }



    @Override
    @Transactional
    public void changeRole(Long id, GroupRoleEnum groupRoleEnum) {
        this.update(new UpdateWrapper<GroupMember>().eq("id",id).set("role",groupRoleEnum.getType()));
    }
    @Override
    @Transactional
    public void changeRoleBatch(Long groupId, List<Long> uidList, GroupRoleEnum groupRoleEnum) {
        this.update(new UpdateWrapper<GroupMember>()
                .eq("group_id",groupId)
                .in("id",uidList)
                .set("role",groupRoleEnum.getType()));
    }

    @Override
    public Map<Long, Integer> getMemberRoleBatch(Long groupId, List<Long> groupMemberList) {
        List<GroupMember> groupMembers = this.list(new QueryWrapper<GroupMember>().eq("group_id", groupId).in("uid", groupMemberList));

        return groupMembers.stream().collect(Collectors.toMap(GroupMember::getUid, GroupMember::getRole));
    }

    @Override
    @Transactional
    public void removeAllGroupMember(Long groupId) {
            this.remove(new QueryWrapper<GroupMember>().eq("group_id",groupId));
            clearGroupRedis(groupId);
    }

    @Override
    @Transactional
    public void addMember(Long uid, Long groupId, GroupRoleEnum groupRoleEnum) {
        GroupMember groupMember = RoomAdapter.buildGroupMember(uid, groupId, groupRoleEnum);

        this.save(groupMember);
        clearGroupRedis(groupId);
    }

    @Override
    @Transactional
    public void addMemberBatch(List<Long> uidList, Long groupId, GroupRoleEnum groupRoleEnum) {
        List<GroupMember> groupMemberList = uidList.stream().map(uid -> RoomAdapter.buildGroupMember(uid, groupId, groupRoleEnum)).toList();
        this.saveBatch(groupMemberList);
        clearGroupRedis(groupId);
    }




    private void clearGroupRedis(Long groupId) {
        String key = GROUP_MEMBER_LIST_PREFIX + groupId;
        stringRedisTemplate.delete(key);
    }

}




