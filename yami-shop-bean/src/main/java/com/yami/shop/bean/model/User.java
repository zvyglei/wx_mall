/*

 */

package com.yami.shop.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
/**
 * @author lanhai
 */
@Data
@TableName("tz_user")
public class User implements Serializable {
    private static final long serialVersionUID = 2090714647038636896L;
    /**
     * ID
     */
    @TableId(type = IdType.INPUT)
    private String userId;

    /**
     * 微信openid
     */
    private String wxOpenId;

    /**
     * 手机号码
     */
    private String userMobile;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */

    private String realName;

    /**
     * 身份证号
     */
    private String idCard;


    /**
     * 身份证人像面照片
     */
    private String idCardImg1;


    /**
     * 身份证国徽面照片
     */
    private String idCardImg2;

    /**
     * M(男) or F(女)
     */
    private String sex;

    /**
     * 例如：2009-11-27
     */

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private String birthDate;

    /**
     * 用户邮箱
     */

    private String userMail;

    /**
     * 登录密码
     */

    private String loginPassword;

    /**
     * 支付密码
     */

    private String payPassword;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    /**
     * 注册时间
     */

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date userRegtime;

    /**
     * 注册IP
     */

    private String userRegip;

    /**
     * 最后登录时间
     */

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date userLasttime;

    /**
     * 最后登录IP
     */

    private String userLastip;

    /**
     * 备注
     */

    private String userMemo;

    /**
     * 头像图片路径
     */
    private String pic;

    /**
     * 状态 1 正常 0 无效
     */
    private Integer status;

    /**
     * 认证状态 0-未认证 1-待审核 2-已认证
     */
    private Integer verifyStatus;

    /**
     * 审核意见
     */
    private String verifyRemark;

    /**
     * 积分
     */
    private Integer score;

    /**
     * 推荐人
     */
    private String referee;

    /**
     * 推荐码
     */
    private String refereeCode;
}
