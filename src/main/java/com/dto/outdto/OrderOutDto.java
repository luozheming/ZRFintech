package com.dto.outdto;

import com.pojo.ProjectComment;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderOutDto {
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
     * 业务状态：1-预约，2-接单，3-拒单，4-签约，5-拒签，6-付款成功，7-问答完成，8-评论完成
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
     * 支付状态：0-未支付，1-支付中，2-支付成功，3-支付失败，4-支付超时，5-支付异常
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
     * 评论信息
     */
    ProjectComment projectComment;
}
