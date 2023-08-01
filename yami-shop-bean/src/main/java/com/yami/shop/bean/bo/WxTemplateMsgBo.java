package com.yami.shop.bean.bo;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: zhao
 * @date: 2023/7/20 12:37
 */
@Data
public class WxTemplateMsgBo {
    private String shopScore;

    private String score;

    private Date shopTime;
}
