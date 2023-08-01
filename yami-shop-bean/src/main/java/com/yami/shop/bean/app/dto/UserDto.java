/*

 */

package com.yami.shop.bean.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author lanhai
 */
@Data
public class UserDto {


    @Schema(description = "用户状态：0禁用 1正常", required = true)
    private Integer status;
    @Schema(description = "token", required = true)
    private String token;

    /**
     * 手机号码
     */
    private String userMobile;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * M(男) or F(女)
     */
    private String sex;

    /**
     * 头像图片路径
     */
    private String pic;

    /**
     * 认证状态 0-未认证 1-已认证
     */
    private Integer verifyStatus;

    /**
     * 积分
     */
    private Integer score;

}
