package com.dto.indto;

import lombok.Data;

@Data
public class UserEditDto {
    /**
     * 用户ID，主键
     */
    private String userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 手机号码
     */
    private String phoneNm;
    /**
     * 登录密码
     */
    private String password;
    /**
     * 角色编码：entuser-商户，investor-投资人，FA-融资顾问，visitor-游客，management-管理员
     */
    private String roleCode;
    /**
     * 公司
     */
    private String companyName;
    /**
     * 职称
     */
    private String positionName;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     *  联系电话号码
     */
    private String telephoneNo;
    /**
     * 关注行业
     */
    private String focusFiled;
    /**
     * 投资轮次
     */
    private String finRound;
    /**
     * 自我简介
     */
    private String selfIntroduction;
    /**
     * 关注城市
     */
    private String focusCity;

    /**
     * 短信验证码
     */
    private String captcha;

}
