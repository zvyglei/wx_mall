/*

 */

package com.yami.shop.bean.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author lh
 */
@Data
@Schema(description = "重置密码")
public class UserResetPasswordParam {

	@Schema(description = "旧密码" )
	private String oldPassWord;

	@Schema(description = "密码" )
	private String passWord;
}
