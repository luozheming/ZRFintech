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
     * 业务状态：1-已付款，2-服务中，3-取消中，4-已完成，5-已取消
     */
    private Integer bizStatus;
    /**
     * 用户id
     */
    private String userId;
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
     * 支付状态：1-支付中，2-支付成功，3-支付失败，4-支付异常,5-支付取消
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
    /**
     * 评论星级
     */
    private BigDecimal stars;
    /**
     * 用户回评
     */
    private String reply;
    /**
     * 回复时间
     */
    private Date replyTm;
    /**
     * 电话号码
     */
    private String phoneNm;
    /**
     * 联系人姓名
     */
    private String userName;
    /**
     * 订单处理人
     */
    private String dealBy;

    @Tolerate
    public Order() {}
}
