package com.yami.shop.api.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.yami.shop.bean.bo.WxTemplateMsgBo;
import com.yami.shop.bean.enums.BillType;
import com.yami.shop.bean.event.PaySuccessOrderEvent;
import com.yami.shop.bean.model.Order;
import com.yami.shop.bean.model.OrderItem;
import com.yami.shop.bean.model.User;
import com.yami.shop.bean.model.UserBill;
import com.yami.shop.common.util.Arith;
import com.yami.shop.service.OrderItemService;
import com.yami.shop.service.ProductService;
import com.yami.shop.service.UserBillService;
import com.yami.shop.service.UserService;
import com.yami.shop.service.impl.WxServerService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @description: 默认的订单支付成功后的操作
 * @author: zhao
 * @date: 2023/7/18 11:22
 */
@Component("defaultPaySuccessListener")
@AllArgsConstructor
public class PaySuccessListener {
    private final UserService userService;
    private final UserBillService userBillService;
    private final OrderItemService orderItemService;
    private final ProductService productService;
    private final WxServerService wxServerService;

    /**
     * 订单支付成功后给推荐人分成
     *
     * @param event
     */
    @EventListener(PaySuccessOrderEvent.class)
    public void defaultShopCartEvent(PaySuccessOrderEvent event) {
        List<Order> orders = event.getOrders();
        if (CollUtil.isNotEmpty(orders)) {
            String userId = orders.get(0).getUserId();
            // 计算分成
            double sum = orders.stream().filter(x -> x.getIsPayed() == 1).mapToDouble(com.yami.shop.bean.model.Order::getTotal).sum();
            double score = Math.ceil(Arith.mul(sum, 0.05));
            // 根据用户id，获取推荐人id
            String referee = userService.getRefereeById(userId);
            // 给推荐人添加积分
            if (StringUtils.isNotBlank(referee)) {
                User user = new User();
                user.setUserId(referee);
                user.setScore((int) score);
                userService.updateScoreById(user);

                // 添加账单信息
                UserBill bill = new UserBill();
                bill.setUserId(userId);
                bill.setOrderId(null);
                bill.setBillType(BillType.IN.value());
                bill.setScore((int) score);
                bill.setBillDesc("代理分成");
                bill.setCreateTime(new Date());
                userBillService.save(bill);

                // 发送微信通知
                User byId = userService.getById(user.getUserId());
                ThreadUtil.execAsync(() -> {
                    WxTemplateMsgBo templateMsg = new WxTemplateMsgBo();
                    templateMsg.setScore(String.valueOf(byId.getScore()));
                    templateMsg.setShopScore("+" + (int) score);
                    templateMsg.setShopTime(new Date());
                    wxServerService.sendUnionMsg(templateMsg, user.getWxOpenId());
                });
            }
            // 更新商品销量
            for (Order order : orders) {
                List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderNumber(order.getOrderNumber());
                for (OrderItem orderItem : orderItems) {
                    productService.updateSoldNumByOrderItem(orderItem.getProdId(), orderItem.getProdCount());
                }
            }
        }
    }
}
