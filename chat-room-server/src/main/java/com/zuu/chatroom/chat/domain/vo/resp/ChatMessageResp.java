package com.zuu.chatroom.chat.domain.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResp {

    @Schema(title ="发送者信息")
    private UserInfo fromUser;
    @Schema(title ="消息详情")
    private Message message;

    @Data
    public static class UserInfo {
        @Schema(title ="用户id")
        private Long uid;
    }

    @Data
    public static class Message {
        @Schema(title ="消息id")
        private Long id;
        @Schema(title ="房间id")
        private Long roomId;
        @Schema(title ="消息发送时间")
        private Date sendTime;
        @Schema(title ="消息类型 1正常文本 2.撤回消息")
        private Integer type;
        @Schema(title ="消息内容不同的消息类型")
        private Object body;
        @Schema(title ="消息标记")
        private MessageMark messageMark;
    }

    @Data
    public static class MessageMark {
        @Schema(title ="点赞数")
        private Integer likeCount;
        @Schema(title ="该用户是否已经点赞 0否 1是")
        private Integer userLike;
        @Schema(title ="举报数")
        private Integer dislikeCount;
        @Schema(title ="该用户是否已经举报 0否 1是")
        private Integer userDislike;
    }
}