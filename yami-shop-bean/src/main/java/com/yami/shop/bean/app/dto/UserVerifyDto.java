/*

 */

package com.yami.shop.bean.app.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author lh
 */
@Data
@Schema(description = "会员认证")
public class UserVerifyDto {

	@Schema(description = "身份证号" )
	private String idCard;

	@Schema(description = "真实姓名" )
	private String realName;

	@JsonSerialize(using = ImgJsonSerializer.class)
	@Schema(description = "身份证人像面照片" )
	private String idCardImg1;

	@JsonSerialize(using = ImgJsonSerializer.class)
	@Schema(description = "身份证国徽面照片" )
	private String idCardImg2;
}
