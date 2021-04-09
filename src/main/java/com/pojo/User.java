package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(value = "user")
public class User {
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

    @Tolerate
    public User() {}
}
