package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

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
     * 项目编号
     */
    private String projectNo;
    /**
     * 投资人编号
     */
    private String investorId;
    /**
     * 支付方
     */
    private String openId;
    /**
     * 支付流水号
     */
    private String businessNo;
    /**
     * 支付金额
     */
    private String payAmount;
    /**
     * 支付状态：0-未支付，1-支付中，2-支付成功，3-支付失败，4-支付超时
     */
    private Integer payStatus;
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
