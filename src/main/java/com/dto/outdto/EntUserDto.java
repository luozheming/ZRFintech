package com.dto.outdto;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;

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
     * 项目编号
     */
    private String projectNo;
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
    /**
     * 是否需要联系客服：true-是，false-否
     */
    private Boolean isContactService;
    /**
     * BP申请时间
     */
    private Date bpApplyTime;
    /**
     * 项目创建时间
     */
    private Date projectCreateTime;
    /**
     * 联系投资人最新时间
     */
    private Date commentCreateTime;
}
