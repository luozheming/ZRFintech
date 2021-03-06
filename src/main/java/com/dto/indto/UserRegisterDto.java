package com.dto.indto;

import lombok.Data;

@Data
public class UserRegisterDto {
    /**
     * 手机号码
     */
    private String phoneNm;
    /**
     * 登录密码
     */
    private String password;
    /**
     * 角色编码：ent-商户，investor-投资者
     */
    private String roleCode;
    /**
     * 短信验证码
     */
    private String captcha;
}
