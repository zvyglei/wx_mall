/*

 */

package com.yami.shop.api.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.app.dto.UserDto;
import com.yami.shop.bean.app.dto.UserVerifyDto;
import com.yami.shop.bean.app.param.UserInfoParam;
import com.yami.shop.bean.bo.WxTemplateMsgBo;
import com.yami.shop.bean.enums.BillType;
import com.yami.shop.bean.model.User;
import com.yami.shop.bean.model.UserBill;
import com.yami.shop.bean.param.UserResetPasswordParam;
import com.yami.shop.bean.param.UserTransferParam;
import com.yami.shop.bean.param.UserVerifyParam;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.api.util.SecurityUtils;
import com.yami.shop.security.common.manager.PasswordManager;
import com.yami.shop.service.UserBillService;
import com.yami.shop.service.UserService;
import com.yami.shop.service.impl.WxServerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author lanhai
 */
@RestController
@RequestMapping("/p/user")
@Tag(name = "用户接口")
@AllArgsConstructor
public class UserController {

	private final UserService userService;

	private final UserBillService userBillService;

	private final WxServerService wxServerService;

	private final PasswordManager passwordManager;

	private final PasswordEncoder passwordEncoder;

	/**
	 * 查看用户接口
	 */
	@GetMapping("/userInfo")
	@Operation(summary = "查看用户信息" , description = "根据用户ID（userId）获取用户信息")
	public ServerResponseEntity<UserDto> userInfo() {
		String userId = SecurityUtils.getUser().getUserId();
		User user = userService.getById(userId);
		UserDto userDto = BeanUtil.copyProperties(user, UserDto.class);
		return ServerResponseEntity.success(userDto);
	}

	/**
	 * 查看用户认证信息接口
	 */
	@GetMapping("/userVerifyInfo")
	@Operation(summary = "查看用户认证信息" , description = "根据用户ID（userId）获取用户认证信息")
	public ServerResponseEntity<UserVerifyDto> userVerifyInfo() {
		String userId = SecurityUtils.getUser().getUserId();
		User user = userService.getById(userId);
		UserVerifyDto userVerifyDto = BeanUtil.copyProperties(user, UserVerifyDto.class);
		return ServerResponseEntity.success(userVerifyDto);
	}

	@PutMapping("/setUserInfo")
	@Operation(summary = "设置用户信息" , description = "设置用户信息")
	public ServerResponseEntity<Void> setUserInfo(@RequestBody UserInfoParam userInfoParam) {
		String userId = SecurityUtils.getUser().getUserId();
		User user = new User();
		user.setUserId(userId);
		if (StringUtils.isNotBlank(userInfoParam.getAvatarUrl())) {
			user.setPic(userInfoParam.getAvatarUrl());
		}
		if (StringUtils.isNotBlank(userInfoParam.getNickName())) {
			user.setNickName(userInfoParam.getNickName());
		}
		userService.updateById(user);
		return ServerResponseEntity.success();
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping("/resetPassword")
	public ServerResponseEntity<Boolean> forgotPassword(@RequestBody UserResetPasswordParam userResetPasswordParam) {
		String userId = SecurityUtils.getUser().getUserId();
		User user = userService.getUserByUserId(userId);
		if (user == null) {
			throw new YamiShopBindException("未登录，请先登录");
		}

		String decryptPassword = passwordManager.decryptPassword(userResetPasswordParam.getOldPassWord());
		String password = passwordEncoder.encode(decryptPassword);
		// if (!user.getLoginPassword().equals(password)) {
		// 	throw new YamiShopBindException("旧密码输入错误");
		// }
		User newUser = new User();
		newUser.setUserId(userId);
		newUser.setLoginPassword(password);
		boolean update = userService.updateById(newUser);
		return ServerResponseEntity.success(update);
	}

	@PostMapping("/verify")
	public ServerResponseEntity<String> verify(@RequestBody UserVerifyParam userVerifyParam) {
		User user = Convert.convert(User.class, userVerifyParam);
		user.setVerifyStatus(1);
		user.setUserId(SecurityUtils.getUser().getUserId());
		userService.updateById(user);
		return ServerResponseEntity.success();
	}

	@PostMapping("/transfer")
	@Transactional(rollbackFor = Exception.class)
	public ServerResponseEntity<String> transfer(@RequestBody UserTransferParam userTransferParam) {
		LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
		qw.eq(User::getUserMobile, userTransferParam.getUserMobile()).eq(User::getRealName, userTransferParam.getRealName())
				.select(User::getUserId, User::getStatus, User::getVerifyStatus, User::getWxOpenId, User::getScore);
		User user = userService.getOne(qw);
		if (user == null) {
			throw new YamiShopBindException("未查询到对方账号信息");
		}
		if (user.getStatus() == 0) {
			throw new YamiShopBindException("对方账号异常，无法转账");
		}
		if (user.getVerifyStatus() != 2) {
			throw new YamiShopBindException("对方还未认证，无法转账");
		}

		double score = userTransferParam.getScore();
		// 对方加钱
		user.setScore(score);
		Boolean b = userService.updateScoreById(user);

		// 己方减钱
		String userId = SecurityUtils.getUser().getUserId();
		User self = new User();
		self.setUserId(userId);
		self.setScore(score);
		Boolean b2 = userService.reduceScoreById(self);

		if (b && b2) {
			// 添加账单信息
			UserBill bill = new UserBill();
			bill.setUserId(userId);
			bill.setOrderId(null);
			bill.setBillType(BillType.IN.value());
			bill.setScore(score);
			bill.setBillDesc("黄金豆到账");
			bill.setCreateTime(new Date());
			userBillService.save(bill);

			// 发送微信通知
			ThreadUtil.execAsync(() -> {
				WxTemplateMsgBo templateMsg = new WxTemplateMsgBo();
				templateMsg.setScore(String.valueOf(user.getScore() + score));
				templateMsg.setShopScore("+" + score);
				templateMsg.setShopTime(new Date());
				wxServerService.sendUnionInMsg(templateMsg, user.getWxOpenId(), "黄金豆到账");
			});


			// 添加账单信息
			UserBill bill2 = new UserBill();
			bill2.setUserId(userId);
			bill2.setOrderId(null);
			bill2.setBillType(BillType.OUT.value());
			bill2.setScore(score);
			bill2.setBillDesc("转账给：" + userTransferParam.getUserMobile());
			bill2.setCreateTime(new Date());
			userBillService.save(bill2);

			// 发送微信通知
			User byId2 = userService.getById(userId);
			ThreadUtil.execAsync(() -> {
				WxTemplateMsgBo templateMsg = new WxTemplateMsgBo();
				templateMsg.setScore(String.valueOf(byId2.getScore() + score));
				templateMsg.setShopScore("-" + score);
				templateMsg.setShopTime(new Date());
				wxServerService.sendUnionOutMsg(templateMsg, byId2.getWxOpenId());
			});
		}

		return ServerResponseEntity.success();
	}

	/**
	 * 配置信息
	 */
	@GetMapping("/transfer/{id}")
	public ServerResponseEntity<Integer> transferConfig(@PathVariable("id") Long id){
		Integer val = userService.getTransferConfig(id);
		return ServerResponseEntity.success(val);
	}

}
