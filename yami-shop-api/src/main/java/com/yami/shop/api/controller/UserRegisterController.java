package com.yami.shop.api.controller;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.model.User;
import com.yami.shop.bean.param.UserRegisterParam;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.util.RedisUtil;
import com.yami.shop.security.common.bo.UserInfoInTokenBO;
import com.yami.shop.security.common.enums.SysTypeEnum;
import com.yami.shop.security.common.manager.PasswordManager;
import com.yami.shop.security.common.manager.TokenStore;
import com.yami.shop.security.common.vo.TokenInfoVO;
import com.yami.shop.service.UserService;
import com.yami.shop.service.impl.WxServerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import com.yami.shop.common.response.ServerResponseEntity;
import me.chanjar.weixin.common.service.WxService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Date;

/**
 * 用户信息
 *
 * @author SJL
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户注册相关接口")
@AllArgsConstructor
public class UserRegisterController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final TokenStore tokenStore;

    private final PasswordManager passwordManager;

    private final WxServerService wxServerService;

    private static final String REGISTER_CODE_KEY_PREFIX = "user:code:register:";
    private static final String FORGOT_CODE_KEY_PREFIX = "user:code:forget:";
    private static final String FORGOT_PAY_CODE_KEY_PREFIX = "user:code:forget-pay:";

    @PostMapping("/register")
    @Operation(summary = "注册" , description = "用户注册或绑定手机号接口")
    public ServerResponseEntity<TokenInfoVO> register(@Valid @RequestBody UserRegisterParam userRegisterParam) {
        // if (StrUtil.isBlank(userRegisterParam.getNickName())) {
        //     userRegisterParam.setNickName(userRegisterParam.getUserMobile());
        // }
        String userMobile = userRegisterParam.getUserMobile();
        // 正在进行申请注册
        if (userService.count(new LambdaQueryWrapper<User>().eq(User::getUserMobile, userMobile)) > 0) {
            // 该手机号已注册，无法重新注册
            throw new YamiShopBindException("该手机号已注册，无法重新注册");
        }

        String key = REGISTER_CODE_KEY_PREFIX + userMobile;
        Object o = RedisUtil.get(key);
        if (o == null) {
            throw new YamiShopBindException("验证码失效，请重新获取");
        }
        if (!o.toString().equals(userRegisterParam.getCode())) {
            throw new YamiShopBindException("验证码错误！");
        }
        // 删除验证码
        RedisUtil.del(userMobile);

        Date now = new Date();
        User user = new User();
        user.setWxOpenId(wxServerService.getOpenId(userRegisterParam.getWxcode()));
        user.setUserMobile(userMobile);
        user.setModifyTime(now);
        user.setUserRegtime(now);
        user.setStatus(1);
        // user.setNickName(userRegisterParam.getNickName());
        user.setUserMail(userRegisterParam.getUserMail());
        String decryptPassword = passwordManager.decryptPassword(userRegisterParam.getPassWord());
        user.setLoginPassword(passwordEncoder.encode(decryptPassword));
        user.setPayPassword(userRegisterParam.getPayPassWord());
        String userId = IdUtil.simpleUUID();
        user.setUserId(userId);
        userService.save(user);
        // 2. 登录
        UserInfoInTokenBO userInfoInTokenBO = new UserInfoInTokenBO();
        userInfoInTokenBO.setUserId(user.getUserId());
        userInfoInTokenBO.setSysType(SysTypeEnum.ORDINARY.value());
        userInfoInTokenBO.setIsAdmin(0);
        userInfoInTokenBO.setEnabled(true);
        return ServerResponseEntity.success(tokenStore.storeAndGetVo(userInfoInTokenBO));
    }


    @PostMapping("/updatePwd")
    @Operation(summary = "修改密码" , description = "修改密码")
    public ServerResponseEntity<Void> updatePwd(@Valid @RequestBody UserRegisterParam userPwdUpdateParam) {
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserMobile, userPwdUpdateParam.getUserMobile()));
        if (user == null) {
            // 无法获取用户信息
            throw new YamiShopBindException("无法获取用户信息");
        }
        String key = FORGOT_CODE_KEY_PREFIX + userPwdUpdateParam.getUserMobile();
        Object o = RedisUtil.get(key);
        if (o == null) {
            throw new YamiShopBindException("验证码失效，请重新获取");
        }
        if (!o.toString().equals(userPwdUpdateParam.getCode())) {
            throw new YamiShopBindException("验证码错误！");
        }
        if (StrUtil.isBlank(userPwdUpdateParam.getPassWord())) {
            // 新密码不能为空
            throw new YamiShopBindException("新密码不能为空");
        }
        String decryptPassword = passwordManager.decryptPassword(userPwdUpdateParam.getPassWord());
        String password = passwordEncoder.encode(decryptPassword);
        // if (StrUtil.equals(password, user.getLoginPassword())) {
        //     // 新密码不能与原密码相同
        //     throw new YamiShopBindException("新密码不能与原密码相同");
        // }
        user.setModifyTime(new Date());
        user.setLoginPassword(password);
        userService.updateById(user);
        return ServerResponseEntity.success();
    }

    @PostMapping("/updatePayPwd")
    public ServerResponseEntity<String> forgotPassword(@RequestBody UserRegisterParam userPwdUpdateParam) {
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserMobile, userPwdUpdateParam.getUserMobile()));
        if (user == null) {
            // 无法获取用户信息
            throw new YamiShopBindException("无法获取用户信息");
        }
        String key = FORGOT_PAY_CODE_KEY_PREFIX + userPwdUpdateParam.getUserMobile();
        Object o = RedisUtil.get(key);
        if (o == null) {
            throw new YamiShopBindException("验证码失效，请重新获取");
        }
        if (!o.toString().equals(userPwdUpdateParam.getCode())) {
            throw new YamiShopBindException("验证码错误！");
        }
        if (StrUtil.isBlank(userPwdUpdateParam.getPayPassWord())) {
            // 新密码不能为空
            throw new YamiShopBindException("新支付密码不能为空");
        }
        // String decryptPassword = passwordManager.decryptPassword(userPwdUpdateParam.getPayPassWord());
        // if (StrUtil.equals(decryptPassword, user.getPayPassword())) {
        //     // 新密码不能与原密码相同
        //     throw new YamiShopBindException("新支付密码不能与原支付密码相同");
        // }
        user.setModifyTime(new Date());
        user.setPayPassword(userPwdUpdateParam.getPayPassWord());
        userService.updateById(user);
        return ServerResponseEntity.success();
    }
}
