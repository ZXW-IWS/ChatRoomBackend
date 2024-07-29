package com.zuu.chatroom.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuu.chatroom.chat.domain.po.GroupMember;
import com.zuu.chatroom.chat.mapper.GroupMemberMapper;
import com.zuu.chatroom.chat.service.GroupMemberService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
        if(Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))){
            //redis中存在
            List<Long> memberUidList = Objects.requireNonNull(stringRedisTemplate.opsForList().range(key, 0, -1))
                    .stream().map(Long::parseLong).toList();
            //刷新有效时间
            stringRedisTemplate.expire(key,GROUP_MEMBER_LIST_MINUTES, TimeUnit.MINUTES);
            return memberUidList;
        }else{
            //redis中不存在
            List<GroupMember> groupMembers = this.list(new QueryWrapper<GroupMember>().eq("group_id", groupId));
            List<Long> groupMemberUidList = groupMembers.stream().map(GroupMember::getUid).toList();
            //存入redis中
            stringRedisTemplate.opsForList().rightPushAll(key,groupMemberUidList.stream().map(Objects::toString).toList());
            stringRedisTemplate.expire(key,GROUP_MEMBER_LIST_MINUTES, TimeUnit.MINUTES);
            return groupMemberUidList;
        }
    }
}




