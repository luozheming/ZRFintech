package com.dto.indto;

import lombok.Data;

@Data
public class UserUpdatePasswordDto {
    /**
     * 手机号码
     */
    private String phoneNm;
    /**
     * 原密码
     */
    private String orgPassword;
    /**
     * 新密码
     */
    private String password;
    /**
     * 验证码
     */
    private String captcha;
}
