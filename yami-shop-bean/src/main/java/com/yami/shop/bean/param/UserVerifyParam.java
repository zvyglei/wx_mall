/*

 */

package com.yami.shop.bean.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author lh
 */
@Data
@Schema(description = "会员认证")
public class UserVerifyParam {

	@Schema(description = "用户id" )
	private Long userId;

	@Schema(description = "身份证号" )
	private String idCard;

	@Schema(description = "真实姓名" )
	private String realName;

	@Schema(description = "身份证人像面照片" )
	private String idCardImg1;

	@Schema(description = "身份证国徽面照片" )
	private String idCardImg2;
}
