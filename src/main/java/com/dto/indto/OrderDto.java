package com.dto.indto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDto {
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 支付方
     */
    private String openId;
    /**
     * 支付金额
     */
    private BigDecimal payAmount;
    /**
     * 业务id(如：评论id,活动id)
     */
    private String bizId;
    /**
     * 业务类型
     */
    private Integer bizType;
    /**
     * 支付类型；1-线上，2-线下
     */
    private Integer paymentType;
}
