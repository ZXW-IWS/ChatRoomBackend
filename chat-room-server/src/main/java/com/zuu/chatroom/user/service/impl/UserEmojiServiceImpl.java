package com.zuu.chatroom.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuu.chatroom.common.annotation.RedissonLock;
import com.zuu.chatroom.common.exception.BusinessException;
import com.zuu.chatroom.user.domain.po.UserEmoji;
import com.zuu.chatroom.user.domain.vo.req.UserEmojiReq;
import com.zuu.chatroom.user.domain.vo.resp.UserEmojiResp;
import com.zuu.chatroom.user.mapper.UserEmojiMapper;
import com.zuu.chatroom.user.service.UserEmojiService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
* @author zuu
* @description 针对表【user_emoji(用户表情包)】的数据库操作Service实现
* @createDate 2024-09-14 11:09:53
*/
@Service
public class UserEmojiServiceImpl extends ServiceImpl<UserEmojiMapper, UserEmoji>
    implements UserEmojiService {

    public static final int MAX_EMOJI_COUNT = 20;

    @Override
    public List<UserEmojiResp> emojiList(Long uid) {
        List<UserEmoji> userEmojis = this.list(new QueryWrapper<UserEmoji>().eq("uid", uid));
        return userEmojis.stream().map(userEmoji -> {
            UserEmojiResp userEmojiResp = new UserEmojiResp();
            userEmojiResp.setId(userEmoji.getId());
            userEmojiResp.setExpressionUrl(userEmoji.getExpressionUrl());

            return userEmojiResp;
        }).toList();
    }

    @Override
    @RedissonLock(key = "#uid"+"_emoji")
    @Transactional
    public Long insertEmoji(UserEmojiReq userEmojiReq, Long uid) {
        long count = this.count(new QueryWrapper<UserEmoji>().eq("uid", uid));
        if(count > MAX_EMOJI_COUNT){
            throw new BusinessException("一个人最多只能添加" + MAX_EMOJI_COUNT + "个表情");
        }
        UserEmoji emoji = this.getOne(new QueryWrapper<UserEmoji>().eq("uid", uid).eq("expression_url", userEmojiReq.getExpressionUrl()));
        if(Objects.nonNull(emoji)){
            throw new BusinessException("您已经拥有该表情了");
        }
        emoji = new UserEmoji();
        emoji.setUid(uid);
        emoji.setExpressionUrl(userEmojiReq.getExpressionUrl());
        this.save(emoji);

        return emoji.getId();
    }

    @Override
    @Transactional
    public void removeEmoji(Long emojiId, Long uid) {
        UserEmoji emoji = this.getById(emojiId);
        if(Objects.isNull(emoji)){
            throw new BusinessException("该表情不存在");
        }
        if(!Objects.equals(uid,emoji.getUid())){
            throw new BusinessException("小黑子，别人的表情不是你能动的");
        }
        this.removeById(emoji);
    }
}




