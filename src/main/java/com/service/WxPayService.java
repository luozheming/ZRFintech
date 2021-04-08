package com.service;

import com.dto.outdto.WxPayDto;

import java.math.BigDecimal;

public interface WxPayService {
    WxPayDto wxPay(String openId, BigDecimal payAmount) throws Exception;
    String verifyNotify(String requestData) throws Exception;
}
