package com.dto.outdto;

import lombok.Data;

@Data
public class WxPayDto {
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 时间戳
     */
    private String timeStamp;
    /**
     * 随机字符串
     */
    private String nonceStr;
    /**
     * 订单详情扩展字符串
     */
    private String prepay_id;
    /**
     * 签名方式
     */
    private String signType;
    /**
     * 签名
     */
    private String paySign;
    /**
     * 订单号
     */
    private String outTradeNo;
}
