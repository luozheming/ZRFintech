package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(value = "smsCaptcha")
public class SmsCaptcha {
    /**
     * 主键id
     */
    private String id;
    /**
     * 手机号码
     */
    private String phoneNm;
    /**
     * 验证码
     */
    private String captcha;
    /**
     * 到期时间
     */
    private Date expires;
    /**
     * 状态：0-有效，1-失效
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;

    @Tolerate
    public SmsCaptcha() {}
}
