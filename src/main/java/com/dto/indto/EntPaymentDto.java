package com.dto.indto;

import lombok.Data;

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
     * 投资人ID
     */
    private String investorId;
    /**
     * 投资人姓名
     */
        private String investor;
    /**
     *用户唯一识别键
     */
    private String openId;
}