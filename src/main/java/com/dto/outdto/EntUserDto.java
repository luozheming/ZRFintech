package com.dto.outdto;

import com.pojo.Project;
import lombok.Data;

@Data
public class EntUserDto {
    /**
     * 用户id
     */
    private String userId;
    /**
     *手机号码
     */
    private String phoneNm;
    /**
     * 是否认证
     */
    private Boolean isVerify;
    /**
     * 是否金卡会员
     */
    private Boolean isVipCard;
    /**
     * 项目信息
     */
    private Project project;
    /**
     * 是否有投递记录
     */
    private Boolean isDeliver;
}
