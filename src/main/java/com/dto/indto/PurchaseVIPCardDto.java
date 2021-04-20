package com.dto.indto;

import lombok.Data;

@Data
public class PurchaseVIPCardDto {
    /**
     * 用户openId
     */
    private String openId;
    /**
     * vip卡的id
     */
    private String cardId;
    /**
     * 购买卡的张数
     */
    private Integer cardCount;
}
