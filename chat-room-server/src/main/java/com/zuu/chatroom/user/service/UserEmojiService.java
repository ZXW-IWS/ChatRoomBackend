package com.zuu.chatroom.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zuu.chatroom.common.domain.vo.resp.ApiResult;
import com.zuu.chatroom.user.domain.po.UserEmoji;
import com.zuu.chatroom.user.domain.vo.req.UserEmojiReq;
import com.zuu.chatroom.user.domain.vo.resp.UserEmojiResp;

import java.util.List;

/**
* @author zuu
* @description 针对表【user_emoji(用户表情包)】的数据库操作Service
* @createDate 2024-09-14 11:09:53
*/
public interface UserEmojiService extends IService<UserEmoji> {

    List<UserEmojiResp> emojiList(Long uid);

    Long insertEmoji(UserEmojiReq userEmojiReq, Long uid);

    void removeEmoji(Long emojiId, Long uid);
}
