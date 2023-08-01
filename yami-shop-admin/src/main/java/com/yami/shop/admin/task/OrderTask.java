/*

 */

package com.yami.shop.admin.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.yami.shop.bean.enums.OrderStatus;
import com.yami.shop.bean.model.Order;
import com.yami.shop.bean.model.OrderItem;
import com.yami.shop.service.OrderService;
import com.yami.shop.service.ProductService;
import com.yami.shop.service.SkuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


/**
 * @author FrozenWatermelon
 * 定时任务的配置，请查看xxl-job的java配置文件。
 * @see com.yami.shop.admin.config.XxlJobConfig
 */
@Component
public class OrderTask {


    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private SkuService skuService;

    @Value("${spring.profiles.active}")
    private String profilesActive;

    @Scheduled(cron ="0 */1 * * * ?")
    public void cancelOrder(){
        if ("dev".equals(profilesActive)) {
            return;
        }

        Date now = new Date();
        logger.info("取消超时未支付订单。。。");
        // 获取30分钟之前未支付的订单
        List<Order> orders = orderService.listOrderAndOrderItems(OrderStatus.UNPAY.value(),DateUtil.offsetMinute(now, -30));
        if (CollectionUtil.isEmpty(orders)) {
            return;
        }
        orderService.cancelOrders(orders);
        for (Order order : orders) {
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                productService.removeProductCacheByProdId(orderItem.getProdId());
                skuService.removeSkuCacheBySkuId(orderItem.getSkuId(),orderItem.getProdId());
            }
        }
    }

    /**
     * 确认收货
     */
    @Scheduled(cron ="0 */3 * * * ?")
    public void confirmOrder(){
        if ("dev".equals(profilesActive)) {
            return;
        }

        Date now = new Date();
        logger.info("系统自动确认收货订单。。。");
        // 获取15天之前未支付的订单
        List<Order> orders = orderService.listOrderAndOrderItems(OrderStatus.CONSIGNMENT.value(),DateUtil.offsetDay(now, -15));
        if (CollectionUtil.isEmpty(orders)) {
            return;
        }
        orderService.confirmOrder(orders);
        for (Order order : orders) {
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                productService.removeProductCacheByProdId(orderItem.getProdId());
                skuService.removeSkuCacheBySkuId(orderItem.getSkuId(),orderItem.getProdId());
            }
        }
    }

}
