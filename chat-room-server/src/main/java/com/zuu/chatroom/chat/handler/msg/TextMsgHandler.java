package com.zuu.chatroom.chat.handler.msg;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.zuu.chatroom.chat.domain.entity.MessageExtra;
import com.zuu.chatroom.chat.domain.enums.GroupRoleEnum;
import com.zuu.chatroom.chat.domain.enums.MsgTypeEnum;
import com.zuu.chatroom.chat.domain.po.GroupMember;
import com.zuu.chatroom.chat.domain.po.Message;
import com.zuu.chatroom.chat.domain.po.RoomGroup;
import com.zuu.chatroom.chat.domain.vo.req.ChatMessageReq;
import com.zuu.chatroom.chat.domain.vo.req.TextMsgReq;
import com.zuu.chatroom.chat.domain.vo.resp.TextMsgResp;
import com.zuu.chatroom.chat.handler.factory.MsgHandlerFactory;
import com.zuu.chatroom.chat.service.GroupMemberService;
import com.zuu.chatroom.chat.service.MessageService;
import com.zuu.chatroom.chat.service.RoomGroupService;
import com.zuu.chatroom.chat.service.adapter.MessageAdapter;
import com.zuu.chatroom.common.domain.enums.YesOrNoEnum;
import com.zuu.chatroom.common.exception.BusinessException;
import com.zuu.chatroom.common.utils.ChatUtils;
import com.zuu.chatroom.user.domain.enums.RoleEnum;
import com.zuu.chatroom.user.domain.po.User;
import com.zuu.chatroom.user.service.RoleService;
import com.zuu.chatroom.user.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Handler;
import java.util.stream.Collectors;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/22 15:23
 */
@Component
public class TextMsgHandler implements MsgHandler {
    public static final int CALLBACK_MAX_GAP_COUNT = 99;
    @Resource
    private MessageService messageService;
    @Resource
    UserService userService;
    @Resource
    RoomGroupService roomGroupService;
    @Resource
    GroupMemberService groupMemberService;
    @Resource
    RoleService roleService;
    @Override
    public MsgTypeEnum getMsgTypeEnum() {
        return MsgTypeEnum.TEXT;
    }

    /**
     * @param chatMessageReq 前端发送的消息
     * @param uid   发送消息的用户uid
     * @return
     */
    @Override
    @Transactional
    public Long checkAndSaveMsg(ChatMessageReq chatMessageReq, Long uid) {
        //1.校验注解信息
        String bodyJson = JSONUtil.toJsonStr(chatMessageReq.getBody());
        TextMsgReq textMsgReq = JSONUtil.toBean(bodyJson, TextMsgReq.class);
        ChatUtils.allCheckValidateThrow(textMsgReq);
        //设置消息的基本信息
        checkMsg(chatMessageReq, uid, textMsgReq);
        //保存消息
        Message message = MessageAdapter.buildBaseMessage(chatMessageReq,uid);
        message.setContent(textMsgReq.getContent());
        messageService.save(message);
        //额外信息保存
        //回复消息相关信息保存
        if(Objects.nonNull(textMsgReq.getReplyMsgId())){
            Integer gapCount = messageService.getGapCount(chatMessageReq.getRoomId(),textMsgReq.getReplyMsgId(),message.getId());
            message.setGapCount(gapCount);
            message.setReplyMsgId(textMsgReq.getReplyMsgId());
        }
        //@成员列表保存
        MessageExtra extra = Optional.ofNullable(message.getExtra()).orElse(new MessageExtra());
        if(CollectionUtil.isNotEmpty(textMsgReq.getAtUidList())){
            extra.setAtUidList(textMsgReq.getAtUidList());
            message.setExtra(extra);
        }
        messageService.updateById(message);

        return message.getId();
    }

    private void checkMsg(ChatMessageReq chatMessageReq, Long uid, TextMsgReq textMsgReq) {
        //校验回复消息
        if(Objects.nonNull(textMsgReq.getReplyMsgId())){
            Message replyMsg = messageService.getById(textMsgReq.getReplyMsgId());
            if(Objects.isNull(replyMsg)){
                throw new BusinessException("回复的消息不存在");
            }
            if(!Objects.equals(replyMsg.getRoomId(), chatMessageReq.getRoomId())){
                throw new BusinessException("只能回复本房间的消息");
            }
        }
        //检验@列表信息
        if(CollectionUtil.isEmpty(textMsgReq.getAtUidList())){
            return;
        }
        //去重操作
        List<Long> atUidList = new HashSet<>(textMsgReq.getAtUidList()).stream().toList();
        //是否@全员
        boolean isAtAll = atUidList.contains(0L);
        if(isAtAll){
            //检验是否有权限
            RoomGroup roomGroup = roomGroupService.getByRoomId(chatMessageReq.getRoomId());
            GroupMember groupMember = groupMemberService.getMember(uid, roomGroup.getId());
            boolean hasPower = Objects.equals(groupMember.getRole(), GroupRoleEnum.LEADER.getType())
                    || Objects.equals(groupMember.getRole(),GroupRoleEnum.MANAGER.getType())
                    || roleService.hasPower(uid, RoleEnum.SUPER_ADMIN);
            if(!hasPower){
                throw new BusinessException("您没有权限@全体成员");
            }
        }else{
            //校验@的用户是否都存在
            List<User> atUserList = userService.listByIds(atUidList);
            if(!Objects.equals(atUserList.size(),atUidList.size())){
                throw new BusinessException("您@的用户不存在");
            }
        }
    }


    @Override
    public Object showMsg(Message msg) {
        TextMsgResp textMsgResp = new TextMsgResp();
        textMsgResp.setContent(msg.getContent());
        //extra为空则Optional为空，map不执行
        textMsgResp.setAtUidList(Optional.ofNullable(msg.getExtra()).map(MessageExtra::getAtUidList).orElse(null));
        //回复消息组装
        if(Objects.nonNull(msg.getReplyMsgId())){
            Message replyMsg = messageService.getById(msg.getReplyMsgId());
            if(Objects.nonNull(replyMsg)){
                TextMsgResp.ReplyMsg reply = new TextMsgResp.ReplyMsg();
                reply.setId(replyMsg.getId());
                reply.setUid(replyMsg.getFromUid());
                User replyUser = userService.getById(replyMsg.getFromUid());
                reply.setUsername(replyUser.getNickname());
                reply.setType(replyMsg.getType());
                reply.setBody(MsgHandlerFactory.getHandlerNotNull(replyMsg.getType()).showReplyMsg(replyMsg));
                //gapCount信息是保存在本消息中的
                reply.setCanCallback(canCallBack(msg));
                reply.setGapCount(msg.getGapCount());

                textMsgResp.setReply(reply);
            }
        }
        return textMsgResp;
    }

    private Integer canCallBack(Message msg) {
        return (Objects.nonNull(msg.getGapCount()) && msg.getGapCount() <= CALLBACK_MAX_GAP_COUNT)
                ? YesOrNoEnum.YES.getStatus()
                : YesOrNoEnum.NO.getStatus();
    }

    @Override
    public Object showReplyMsg(Message msg) {
        return msg.getContent();
    }

    @Override
    public String showContactMsg(Message msg) {
        return msg.getContent();
    }
}
