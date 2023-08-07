/*

 */

package com.yami.shop.bean.app.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author lanhai
 */
@Schema(description = "首页兑换记录对象")
@Data
public class IndexOrderDto {

    private String realName;

    private String userMobile;

    /**
     * 图片
     */
    @JsonSerialize(using = ImgJsonSerializer.class)
    @Schema(description = "图片Url" , required = true)
    private String pic;

    private String prodName;

    private String prodCount;

    private Double price;

    private String oriPrice;
}
