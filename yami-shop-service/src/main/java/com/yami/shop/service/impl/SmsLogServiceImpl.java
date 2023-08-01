/*

 */

package com.yami.shop.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.bean.app.param.SendSmsParam;
import com.yami.shop.bean.enums.SmsType;
import com.yami.shop.bean.model.SmsLog;
import com.yami.shop.common.bean.AliDaYu;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.util.RedisUtil;
import com.yami.shop.dao.SmsLogMapper;
import com.yami.shop.service.SmsLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;


/**
 * @author lgh on 2018/11/29.
 */
@Service
@Slf4j
@AllArgsConstructor
public class SmsLogServiceImpl extends ServiceImpl<SmsLogMapper, SmsLog> implements SmsLogService {

    // private final SmsLogMapper smsLogMapper;

    private final AliDaYu aLiDaYu;

    /**
     * 产品域名,开发者无需替换
     */
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void sendSms(SendSmsParam sendSmsParam) {
        String mobile = sendSmsParam.getMobile();
        String type = sendSmsParam.getType();
        String redisKey = "user:code:" + type + ":" + mobile;

        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(aLiDaYu.getAccessKeyId())
                // 您的AccessKey Secret
                .setAccessKeySecret(aLiDaYu.getAccessKeySecret());
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = DOMAIN;
        Client client;
        try {
            client = new Client(config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JSONObject tp = new JSONObject();
        String code = RandomUtil.randomNumbers(6);
        tp.put("code", code);
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName(aLiDaYu.getSignName())
                .setTemplateCode(aLiDaYu.getTemplateCode())
                .setPhoneNumbers(mobile)
                .setTemplateParam(JSON.toJSONString(tp));
        RuntimeOptions runtime = new RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, runtime);
            if (sendSmsResponse.getStatusCode() == HttpStatus.HTTP_OK) {
                // 保存验证码到redis，保存五分钟
                RedisUtil.set(redisKey, code, 5 * 60);
            }
        } catch (TeaException error) {
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
    }
}
