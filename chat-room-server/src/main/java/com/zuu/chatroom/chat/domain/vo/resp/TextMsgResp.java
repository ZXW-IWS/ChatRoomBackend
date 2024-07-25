package com.zuu.chatroom.chat.domain.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextMsgResp {
    @Schema(title ="消息内容")
    private String content;
    @Schema(title ="消息链接映射")
    private Map<String, UrlInfo> urlContentMap;
    @Schema(title ="艾特的uid")
    private List<Long> atUidList;
    @Schema(title ="父消息，如果没有父消息，返回的是null")
    private TextMsgResp.ReplyMsg reply;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReplyMsg {
        @Schema(title ="消息id")
        private Long id;
        @Schema(title ="用户uid")
        private Long uid;
        @Schema(title ="用户名称")
        private String username;
        @Schema(title ="消息类型 1正常文本 2.撤回消息")
        private Integer type;
        @Schema(title ="消息内容不同的消息类型，见父消息内容体")
        private Object body;
        @Schema(title ="是否可消息跳转 0否 1是")
        private Integer canCallback;
        @Schema(title ="跳转间隔的消息条数")
        private Integer gapCount;
    }
}