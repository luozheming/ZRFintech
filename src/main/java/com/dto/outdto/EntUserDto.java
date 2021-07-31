package com.dto.outdto;

import com.pojo.Project;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class EntUserDto {
    /**
     * 用户id
     */
    private String entUserId;
    /**
     *用户唯一识别键
     */
    private String openId;
    /**
     *手机号码
     */
    private String phoneNm;
    /**
     * 项目信息
     */
    private Project project;
}
