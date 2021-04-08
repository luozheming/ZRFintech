package com.dto.indto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDto {
    /**
     * 项目编号
     */
    private String projectNo;
    /**
     * 支付方
     */
    private String openId;
    /**
     * 投资人编号
     */
    private String investorId;
    /**
     * 支付金额
     */
    private BigDecimal payAmount;
}
