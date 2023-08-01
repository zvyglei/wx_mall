/*

 */

package com.yami.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.app.param.SendSmsParam;
import com.yami.shop.bean.enums.SmsType;
import com.yami.shop.bean.model.SmsLog;

import java.util.Map;

/**
 *
 * @author lgh on 2018/11/29.
 */
public interface SmsLogService extends IService<SmsLog> {
	/**
	 * 发送短信
	 * @param mobile 手机号
	 */
	void sendSms(SendSmsParam sendSmsParam);
}
