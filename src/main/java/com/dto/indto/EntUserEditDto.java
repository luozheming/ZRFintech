package com.dto.indto;

import lombok.Data;

@Data
public class EntUserEditDto {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 手机号码
     */
    private String phoneNm;
    /**
     * 用户名称
     */
    private String userName;
}
