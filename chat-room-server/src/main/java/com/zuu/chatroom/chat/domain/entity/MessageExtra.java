package com.zuu.chatroom.chat.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zuu.chatroom.chat.domain.dto.FileMsgDTO;
import com.zuu.chatroom.chat.domain.dto.ImgMsgDTO;
import com.zuu.chatroom.chat.domain.dto.SoundMsgDTO;
import com.zuu.chatroom.chat.domain.dto.VideoMsgDTO;
import com.zuu.chatroom.chat.domain.vo.resp.UrlInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageExtra implements Serializable {
    private static final long serialVersionUID = 1L;
    //url跳转链接
    private Map<String, UrlInfo> urlContentMap;
    //消息撤回详情
    private MsgRecall recall;
    //艾特的uid
    private List<Long> atUidList;
    ////文件消息
    private FileMsgDTO fileMsgDTO;
    ////图片消息
    private ImgMsgDTO imgMsgDTO;
    ////语音消息
    private SoundMsgDTO soundMsgDTO;
    ////文件消息
    private VideoMsgDTO videoMsgDTO;

    /**
     * 表情图片信息
     */
    //private EmojisMsgDTO emojisMsgDTO;
}
