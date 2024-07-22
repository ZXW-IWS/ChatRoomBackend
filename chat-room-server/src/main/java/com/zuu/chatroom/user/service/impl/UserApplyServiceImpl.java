package com.zuu.chatroom.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuu.chatroom.common.domain.vo.req.PageBaseReq;
import com.zuu.chatroom.user.domain.enums.ApplyReadStatusEnum;
import com.zuu.chatroom.user.domain.enums.ApplyStatusEnum;
import com.zuu.chatroom.user.domain.enums.ApplyTypeEnum;
import com.zuu.chatroom.user.domain.po.UserApply;
import com.zuu.chatroom.user.mapper.UserApplyMapper;
import com.zuu.chatroom.user.service.UserApplyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author zuu
* @description 针对表【user_apply(用户申请表)】的数据库操作Service实现
* @createDate 2024-07-18 09:30:26
*/
@Service
public class UserApplyServiceImpl extends ServiceImpl<UserApplyMapper, UserApply>
    implements UserApplyService {

    @Override
    public Page<UserApply> getApplyList(Long uid, PageBaseReq getApplyListReq) {
        //根据申请请求发起的时间排序，优先返回后创建的
        QueryWrapper<UserApply> wrapper = new QueryWrapper<>();
        wrapper.eq("target_id",uid)
                .eq("type", ApplyTypeEnum.ADD_FRIEND.getType())
                .orderByDesc("create_time");
        Integer pageSize = getApplyListReq.getPageSize();
        Integer pageNo = getApplyListReq.getPageNo();
        Page<UserApply> page = new Page<>(pageNo,pageSize);
        return this.page(page, wrapper);
    }

    @Override
    @Transactional
    public void readApples(List<Long> applyIds) {
        List<UserApply> userApplyList = applyIds.stream().map(applyId -> {
            UserApply userApply = new UserApply();
            userApply.setId(applyId);
            userApply.setReadStatus(ApplyReadStatusEnum.READ.getStatus());
            return userApply;
        }).toList();
        this.updateBatchById(userApplyList);
    }

    @Override
    public Long getUnReadCount(Long uid) {
        return this.count(
                new QueryWrapper<UserApply>()
                        .eq("target_id", uid)
                        .eq("read_status", ApplyReadStatusEnum.NO_READ.getStatus()));
    }

    @Override
    public void agree(Long applyId) {
        this.update(
                new UpdateWrapper<UserApply>()
                        .eq("id",applyId)
                        .set("status", ApplyStatusEnum.APPROVED.getStatus()));
    }

    @Override
    public UserApply getApply(Long uid, Long targetUid) {
        return this.getOne(
                new QueryWrapper<UserApply>()
                        .eq("uid", uid)
                        .eq("target_id", targetUid)
                        .eq("type",ApplyTypeEnum.ADD_FRIEND.getType())
                        .eq("status",ApplyStatusEnum.WAIT_APPROVE.getStatus()));
    }

    @Override
    @Transactional
    public Long saveApply(Long uid, Long targetUid, String applyReqMsg) {
        UserApply userApply = new UserApply();
        userApply.setUid(uid);
        userApply.setTargetId(targetUid);
        userApply.setMsg(applyReqMsg);
        userApply.setType(ApplyTypeEnum.ADD_FRIEND.getType());
        userApply.setStatus(ApplyStatusEnum.WAIT_APPROVE.getStatus());
        userApply.setReadStatus(ApplyReadStatusEnum.NO_READ.getStatus());

        this.save(userApply);
        return userApply.getId();
    }
}




