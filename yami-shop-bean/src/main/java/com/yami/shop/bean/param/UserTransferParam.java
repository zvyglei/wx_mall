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
public class UserTransferParam {

	private String userMobile;

	private String realName;

	private Integer score;
}
