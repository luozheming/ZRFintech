package com.dto.outdto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvestorCommentAmountDto {
    /**
     * 投资人ID
     */
    private String investorId;
    /**
     * 投资人未获取的资费金额
     */
    private BigDecimal unaccomplishedAmount;
    /**
     * 投资人已获取的资费金额
     */
    private BigDecimal accomplishedAmount;
    /**
     * 未成交单数
     */
    private Integer unAccomplishedTimes;
    /**
     * 已成交单数
     */
    private Integer accomplishedTimes;

}
