package com.dto.indto;

import lombok.Data;

@Data
public class EntUserEditDto {
    /**
     * 用户id
     */
    private String entUserId;
    /**
     * 手机号码
     */
    private String phoneNm;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 公司
     */
    private String companyName;
    /**
     * 职称
     */
    private String positionName;
}
