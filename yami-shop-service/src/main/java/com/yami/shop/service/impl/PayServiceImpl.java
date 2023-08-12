/*

 */

package com.yami.shop.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.bo.WxTemplateMsgBo;
import com.yami.shop.bean.enums.BillType;
import com.yami.shop.bean.enums.PayType;
import com.yami.shop.bean.event.PaySuccessOrderEvent;
import com.yami.shop.bean.model.Order;
import com.yami.shop.bean.model.OrderSettlement;
import com.yami.shop.bean.app.param.PayParam;
import com.yami.shop.bean.model.User;
import com.yami.shop.bean.model.UserBill;
import com.yami.shop.bean.pay.PayInfoDto;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.util.Arith;
import com.yami.shop.dao.OrderMapper;
import com.yami.shop.dao.OrderSettlementMapper;
import com.yami.shop.dao.ProductMapper;
import com.yami.shop.dao.UserMapper;
import com.yami.shop.service.PayService;
import com.yami.shop.service.UserBillService;
import com.yami.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lgh on 2018/09/15.
 */
@Service
public class PayServiceImpl implements PayService {

    @Autowired
    private UserBillService userBillService;

    @Autowired
    private WxServerService wxServerService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductMapper productMapper;



    @Autowired
    private OrderSettlementMapper orderSettlementMapper;


    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private Snowflake snowflake;

    /**
     * 不同的订单号，同一个支付流水号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayInfoDto pay(String userId, PayParam payParam) {
        // 不同的订单号的产品名称
        StringBuilder prodName = new StringBuilder();
        // 支付单号
        String payNo = String.valueOf(snowflake.nextId());
        String[] orderNumbers = payParam.getOrderNumbers().split(StrUtil.COMMA);
        // 修改订单信息
        for (String orderNumber : orderNumbers) {
            OrderSettlement orderSettlement = new OrderSettlement();
            orderSettlement.setPayNo(payNo);
            orderSettlement.setPayType(payParam.getPayType());
            orderSettlement.setUserId(userId);
            orderSettlement.setOrderNumber(orderNumber);
            orderSettlementMapper.updateByOrderNumberAndUserId(orderSettlement);

            Order order = orderMapper.getOrderByOrderNumber(orderNumber);
            prodName.append(order.getProdName()).append(StrUtil.COMMA);
        }
        // 除了ordernumber不一样，其他都一样
        List<OrderSettlement> settlements = orderSettlementMapper.getSettlementsByPayNo(payNo);
        // 应支付的总金额
        double payAmount = 0.0;
        for (OrderSettlement orderSettlement : settlements) {
            payAmount = Arith.add(payAmount, orderSettlement.getPayAmount());
        }

        // 校验余额
        User user = userMapper.selectById(userId);
        if (user.getScore() < payAmount) {
            throw new YamiShopBindException("余额不足！");
        } else {
            User scoreUser = new User();
            scoreUser.setUserId(userId);
            scoreUser.setScore(payAmount);
            Boolean reduce = userMapper.reduceScoreById(scoreUser);
            if (!reduce) {
                throw new YamiShopBindException("支付失败，请重新选购！");
            }
        }

        // 添加账单信息
        UserBill bill = new UserBill();
        bill.setUserId(userId);
        bill.setOrderId(payParam.getOrderNumbers());
        bill.setBillType(BillType.OUT.value());
        bill.setScore(payAmount);
        bill.setBillDesc("商城消费");
        bill.setCreateTime(new Date());
        userBillService.save(bill);

        // 发送微信通知
        double finalPayAmount = payAmount;
        ThreadUtil.execAsync(() -> {
            WxTemplateMsgBo templateMsg = new WxTemplateMsgBo();
            templateMsg.setScore(String.valueOf(user.getScore() - finalPayAmount));
            templateMsg.setShopScore("-" + finalPayAmount);
            templateMsg.setShopTime(new Date());
            wxServerService.sendUnionOutMsg(templateMsg, user.getWxOpenId());
        });

        prodName.substring(0, Math.min(100, prodName.length() - 1));

        PayInfoDto payInfoDto = new PayInfoDto();
        payInfoDto.setBody(prodName.toString());
        payInfoDto.setPayAmount(payAmount);
        payInfoDto.setPayNo(payNo);
        return payInfoDto;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> paySuccess(String payNo, String bizPayNo) {
        List<OrderSettlement> orderSettlements = orderSettlementMapper.selectList(new LambdaQueryWrapper<OrderSettlement>().eq(OrderSettlement::getPayNo, payNo));

        OrderSettlement settlement = orderSettlements.get(0);

        // 订单已支付
        if (settlement.getPayStatus() == 1) {
            throw new YamiShopBindException("订单已支付");
        }
        // 修改订单结算信息
        if (orderSettlementMapper.updateToPay(payNo, settlement.getVersion()) < 1) {
            throw new YamiShopBindException("结算信息已更改");
        }
        List<String> orderNumbers = orderSettlements.stream().map(OrderSettlement::getOrderNumber).collect(Collectors.toList());

        // 将订单改为已支付状态
        orderMapper.updateByToPaySuccess(orderNumbers, PayType.WECHATPAY.value());

        List<Order> orders = orderNumbers.stream().map(orderNumber -> orderMapper.getOrderByOrderNumber(orderNumber)).collect(Collectors.toList());
        eventPublisher.publishEvent(new PaySuccessOrderEvent(orders));
        return orderNumbers;
    }

}
