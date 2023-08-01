/*

 */

package com.yami.shop.api.controller;

import com.yami.shop.bean.app.param.SendSmsParam;
import com.yami.shop.bean.bo.WxTemplateMsgBo;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.service.SmsLogService;
import com.yami.shop.service.impl.WxServerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author zhao
 */
@RestController
@RequestMapping("/sms")
@Tag(name = "发送验证码接口")
public class SmsController {

    @Autowired
    private SmsLogService smsLogService;
    @Autowired
    private WxServerService wxServerService;

    @GetMapping("wxmsg")
    public void wxmsg(String code) {
        String openId = wxServerService.getOpenId(code);
        WxTemplateMsgBo templateMsg = new WxTemplateMsgBo();
        templateMsg.setScore("399");
        templateMsg.setShopScore("100");
        templateMsg.setShopTime(new Date());
        wxServerService.sendUnionMsg(templateMsg, openId);
    }

    /**
     * 发送验证码接口
     */
    @PostMapping("/send")
    @Operation(summary = "发送验证码", description = "用户的发送验证码")
    public ServerResponseEntity<Void> audit(@RequestBody SendSmsParam sendSmsParam) {
        // String userId = SecurityUtils.getUser().getUserId();
        smsLogService.sendSms(sendSmsParam);

        return ServerResponseEntity.success();
    }
}
