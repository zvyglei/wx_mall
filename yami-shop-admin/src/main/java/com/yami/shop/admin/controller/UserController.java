/*

 */

package com.yami.shop.admin.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.app.dto.UserVerifyDto;
import com.yami.shop.bean.bo.WxTemplateMsgBo;
import com.yami.shop.bean.enums.BillType;
import com.yami.shop.bean.model.User;
import com.yami.shop.bean.model.UserBill;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.UserBillService;
import com.yami.shop.service.UserService;
import com.yami.shop.service.impl.WxServerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * @author lgh on 2018/10/16.
 */
@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserBillService userBillService;

    @Autowired
    private WxServerService wxServerService;

    /**
     * 分页获取
     */
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('admin:user:page')")
    public ServerResponseEntity<IPage<User>> page(User user, PageParam<User> page) {
        IPage<User> userPage = userService.page(page, new LambdaQueryWrapper<User>()
                .like(StrUtil.isNotBlank(user.getNickName()), User::getNickName, user.getNickName())
                .eq(user.getStatus() != null, User::getStatus, user.getStatus())
                .orderByDesc(User::getUserRegtime));
        for (User userResult : userPage.getRecords()) {
            userResult.setNickName(userResult.getNickName() == null ? "" : userResult.getNickName());
        }
        return ServerResponseEntity.success(userPage);
    }

    /**
     * 分页获取
     */
    @GetMapping("/search")
    public ServerResponseEntity<List<User>> search(String keywords) {
        List<User> list = new ArrayList<>();
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.select(User::getUserId, User::getRealName, User::getUserMobile);
        if (StringUtils.isNotBlank(keywords)) {
            qw.like(User::getRealName, keywords).or().like(User::getUserMobile, keywords)
                    .orderByDesc(User::getUserRegtime);
            list = userService.list(qw);
        }
        return ServerResponseEntity.success(list);
    }

    /**
     * 获取信息
     */
    @GetMapping("/info/{userId}")
    @PreAuthorize("@pms.hasPermission('admin:user:info')")
    public ServerResponseEntity<User> info(@PathVariable("userId") String userId) {
        User user = userService.getById(userId);
        user.setNickName(user.getNickName() == null ? "" : user.getNickName());
        return ServerResponseEntity.success(user);
    }

    /**
     * 修改
     */
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin:user:update')")
    public ServerResponseEntity<Void> update(@RequestBody User user) {
        user.setModifyTime(new Date());
        user.setNickName(user.getNickName() == null ? "" : user.getNickName());
        userService.updateById(user);
        return ServerResponseEntity.success();
    }

    /**
     * 删除
     */
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('admin:user:delete')")
    public ServerResponseEntity<Void> delete(@RequestBody String[] userIds) {
        userService.removeByIds(Arrays.asList(userIds));
        return ServerResponseEntity.success();
    }

    /**
     * 充值
     * @param user
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("charge")
    @PreAuthorize("@pms.hasPermission('admin:user:charge')")
    public ServerResponseEntity<Boolean> scoreCharge(@RequestBody User user) {
        user.setModifyTime(new Date());
        Boolean aBoolean = userService.updateScoreById(user);

        String chargeRemark = StringUtils.isNotBlank(user.getChargeRemark()) ? "（" + user.getChargeRemark() + "）" : "";
        // 添加账单信息
        UserBill bill = new UserBill();
        bill.setUserId(user.getUserId());
        bill.setOrderId(null);
        bill.setBillType(BillType.IN.value());
        bill.setScore(user.getScore());
        bill.setBillDesc("黄金豆到账" + chargeRemark);
        bill.setCreateTime(new Date());
        boolean save = userBillService.save(bill);
        // 发送微信通知
        User byId = userService.getById(user.getUserId());
        ThreadUtil.execAsync(() -> {
            WxTemplateMsgBo templateMsg = new WxTemplateMsgBo();
            templateMsg.setScore(String.valueOf(byId.getScore()));
            templateMsg.setShopScore("+" + user.getScore());
            templateMsg.setShopTime(new Date());
            wxServerService.sendUnionInMsg(templateMsg, user.getWxOpenId(), "黄金豆到账" + chargeRemark);
        });
        if (!aBoolean || !save) {
            throw new YamiShopBindException("充值失败，请重试！");
        }

        return ServerResponseEntity.success();
    }

    /**
     * 扣除
     * @param user
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("reduce")
    @PreAuthorize("@pms.hasPermission('admin:user:reduce')")
    public ServerResponseEntity<Boolean> scoreReduce(@RequestBody User user) {
        user.setModifyTime(new Date());
        Boolean aBoolean = userService.reduceScoreById(user);

        // 添加账单信息
        UserBill bill = new UserBill();
        bill.setUserId(user.getUserId());
        bill.setOrderId(null);
        bill.setBillType(BillType.OUT.value());
        bill.setScore(user.getScore());
        bill.setBillDesc("黄金豆扣除");
        bill.setCreateTime(new Date());
        boolean save = userBillService.save(bill);
        // 发送微信通知
        User byId = userService.getById(user.getUserId());
        ThreadUtil.execAsync(() -> {
            WxTemplateMsgBo templateMsg = new WxTemplateMsgBo();
            templateMsg.setScore(String.valueOf(byId.getScore()));
            templateMsg.setShopScore("-" + user.getScore());
            templateMsg.setShopTime(new Date());
            wxServerService.sendUnionOutMsg(templateMsg, byId.getWxOpenId());
        });
        if (!aBoolean || !save) {
            throw new YamiShopBindException("扣除失败，请重试！");
        }

        return ServerResponseEntity.success();
    }

    @GetMapping("examine/info")
    @PreAuthorize("@pms.hasPermission('admin:user:examine')")
    public ServerResponseEntity<UserVerifyDto> examineInfo(String userId) {
        User user = userService.getById(userId);
        UserVerifyDto res = Convert.convert(UserVerifyDto.class, user);
        return ServerResponseEntity.success(res);
    }

    @PostMapping("examine")
    @PreAuthorize("@pms.hasPermission('admin:user:examine')")
    public ServerResponseEntity<Boolean> examineInfo(@RequestBody User user) {
        boolean update = userService.updateById(user);
        // TODO 发送审核通知
        return ServerResponseEntity.success(update);
    }
}
