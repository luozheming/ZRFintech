package com.dto.indto;

import lombok.Data;

@Data
public class EntUserLoginDto {
    /**
     * 手机号码
     */
    private String phoneNm;
    /**
     * 登录密码
     */
    private String password;
}
