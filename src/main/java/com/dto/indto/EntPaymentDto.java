package com.dto.indto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EntPaymentDto {
    /**
     * 项目编号
     */
    private String projectNo;
    /**
     * 项目名称
     */
    private String projectNm;
    /**
     * 项目所在地城市
     */
    private String entCity;
    /**
     * 投资人ID
     */
    private String investorId;
    /**
     * 是否平台投资人：true-是，false-否
     */
    private Boolean isPlatform;
    /**
     * 投资人姓名
     */
    private String investor;
    /**
     * 投资人机构名称
     */
    private String investorOrgNm;
    /**
     * 投资人照片路径
     */
    private String  invesPhotoRoute;
    /**
     *用户唯一识别键
     */
    private String openId;
    /**
     *评论资费金额
     */
    private BigDecimal commentAmount;
    /**
     * 对接方式:1-线上问答，2-线上1V1交流，3-线下1V1交流
     */
    private Integer commentType;
}
