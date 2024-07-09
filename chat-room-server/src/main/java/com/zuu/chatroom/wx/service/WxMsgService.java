package com.zuu.chatroom.wx.service;

import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/7 21:30
 */
public interface WxMsgService {
    /**
     * 扫码成功事件
     * @param wxMpService
     * @param wxMpXmlMessage 微信推送过来的消息，xml格式. https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html
     * @return WxMpXmlOutMessage用于处理微信推送消息.
     */
    WxMpXmlOutMessage scan(WxMpService wxMpService, WxMpXmlMessage wxMpXmlMessage);

    void authorize(WxOAuth2UserInfo userInfo);
}
