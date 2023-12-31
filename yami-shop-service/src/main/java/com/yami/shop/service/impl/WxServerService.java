package com.yami.shop.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateData;
import cn.binarywang.wx.miniapp.bean.WxMaUniformMessage;
import cn.hutool.core.date.DateUtil;
import com.yami.shop.bean.bo.WxTemplateMsgBo;
import com.yami.shop.common.config.WxMaProperties;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;

/**
 * @description:
 * @author: zhao
 * @date: 2023/7/17 22:33
 */
@Service
public class WxServerService {
    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private WxMaProperties properties;

    @Value("${wx.mp.appid}")
    private String mpAppid;

    // 充值消息模板
    private static final String TEMPLATE_RECHARGE_ID = "30vhRCzqeSQG3CkVry65IcQzFy-AZulc4QaSkO-AdJ4";

    // 消费消息模板
    private static final String TEMPLATE_SHOP_ID = "BinQ0_z9yTDkV-j3DacsCOdjiWG-axQkg-ncvufBBo0";

    public void sendUnionInMsg(WxTemplateMsgBo templateMsg, String openId, String desc) {
        String appid = properties.getConfigs().get(0).getAppid();
        WxMaUniformMessage.MiniProgram miniProgram = new WxMaUniformMessage.MiniProgram();
        miniProgram.setAppid(appid);

        WxMaUniformMessage message = WxMaUniformMessage.builder()
                .templateId(TEMPLATE_RECHARGE_ID)
                .isMpTemplateMsg(true)
                .toUser(openId)
                .miniProgram(miniProgram)
                .appid(mpAppid)
                .build();
        message.addData(new WxMaTemplateData("thing11", desc));
        message.addData(new WxMaTemplateData("time4", DateUtil.format(templateMsg.getShopTime(), "yyyy年MM月dd HH:mm:ss")));
        message.addData(new WxMaTemplateData("amount2", templateMsg.getShopScore()));
        message.addData(new WxMaTemplateData("amount3", templateMsg.getScore().toString()));
        try {
            wxMaService.getMsgService().sendUniformMsg(message);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    public void sendUnionOutMsg(WxTemplateMsgBo templateMsg, String openId) {
        String appid = properties.getConfigs().get(0).getAppid();
        WxMaUniformMessage.MiniProgram miniProgram = new WxMaUniformMessage.MiniProgram();
        miniProgram.setAppid(appid);

        WxMaUniformMessage message = WxMaUniformMessage.builder()
                .templateId(TEMPLATE_SHOP_ID)
                .isMpTemplateMsg(true)
                .toUser(openId)
                .miniProgram(miniProgram)
                .appid(mpAppid)
                .build();
        message.addData(new WxMaTemplateData("amount1", templateMsg.getShopScore()));
        message.addData(new WxMaTemplateData("amount5", templateMsg.getScore().toString()));
        message.addData(new WxMaTemplateData("time6", DateUtil.format(templateMsg.getShopTime(), "yyyy年MM月dd HH:mm:ss")));
        try {
            wxMaService.getMsgService().sendUniformMsg(message);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    public String getOpenId(String code) {
        return code2Session(code).getOpenid();
    }

    private WxMaJscode2SessionResult code2Session(String code) {
        try {
            WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(code);
            return sessionInfo;
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }
}
