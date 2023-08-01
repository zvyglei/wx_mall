/*

 */

package com.yami.shop.api.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.app.dto.UserDto;
import com.yami.shop.bean.app.dto.UserVerifyDto;
import com.yami.shop.bean.app.param.UserInfoParam;
import com.yami.shop.bean.model.User;
import com.yami.shop.bean.model.UserBill;
import com.yami.shop.bean.param.UserRegisterParam;
import com.yami.shop.bean.param.UserResetPasswordParam;
import com.yami.shop.bean.param.UserVerifyParam;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.RedisUtil;
import com.yami.shop.security.api.util.SecurityUtils;
import com.yami.shop.security.common.manager.PasswordManager;
import com.yami.shop.service.UserBillService;
import com.yami.shop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhao
 */
@RestController
@RequestMapping("/p/user/bill")
@Tag(name = "用户账单接口")
@AllArgsConstructor
public class UserBillController {

	private final UserBillService userBillService;

	/**
	 * 查看个人账单
	 */
	@GetMapping
	@Operation(summary = "查看个人账单" , description = "根据用户ID（userId）获取个人账单")
	public ServerResponseEntity<List<UserBill>> userInfo() {
		String userId = SecurityUtils.getUser().getUserId();
		List<UserBill> list = userBillService.list(new LambdaQueryWrapper<UserBill>().eq(UserBill::getUserId, userId).orderByDesc(UserBill::getCreateTime));
		return ServerResponseEntity.success(list);
	}
}
