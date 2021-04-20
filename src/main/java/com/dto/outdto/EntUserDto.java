package com.dto.outdto;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
public class EntUserDto {
    /**
     *用户唯一识别键
     */
    @Id
    private String openId;
    /**
     *手机号码
     */
    private String phoneNm;
    /**
     *用户微信昵称
     */
    private String nickName;
    /**
     * 性别
     */
    private String gender;
    /**
     * 项目名称
     */
    private String projectNm;
    /**
     * 订单数
     */
    private Integer orderCount;
    /**
     * 付费金额
     */
    private BigDecimal orderAmount;
    /**
     * 是否为申请BP定制的项目
     */
    private Boolean isBpApply;
    /**
     * 申请BP定制的记录id
     */
    private String bpApplyId;
}
