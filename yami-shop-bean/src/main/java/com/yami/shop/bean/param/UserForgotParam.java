package com.yami.shop.bean.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description:
 * @author: zhao
 * @date: 2023/7/18 15:52
 */
@Data
@Schema(description = "忘记密码")
public class UserForgotParam {
    @Schema(description = "手机号" )
    private String userMobile;

    @Schema(description = "验证码" )
    private String code;
}
