package com.dto.outdto;

import lombok.Data;

@Data
public class UserLoginDto {
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
     * 角色编码：entuser-商户，investor-投资人
     */
    private String roleCode;
    /**
     * 用户头像信息
     */
    private String photo;
}
