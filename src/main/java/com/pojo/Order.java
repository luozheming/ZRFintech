package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@Document(value = "order")
public class Order {
    /**
     * 订单号，主键
     */
    private String orderNo;
    /**
     * 业务id(如：评论id)
     */
    private String bizId;
    /**
     * 业务类型：详细见OrderBizType
     */
    private Integer bizType;
    /**
     * 支付方
     */
    private String openId;
    /**
     * 支付流水号
     */
    private String transactionId;
    /**
     * 支付金额
     */
    private BigDecimal payAmount;
    /**
     * 支付状态：0-未支付，1-支付中，2-支付成功，3-支付失败，4-支付超时，5-支付异常
     */
    private Integer payStatus;
    /**
     * 支付类型；1-线上，2-线下
     */
    private Integer paymentType;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    @Tolerate
    public Order() {}
}
