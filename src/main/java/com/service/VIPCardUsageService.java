package com.service;

import com.dto.indto.PurchaseVIPCardDto;
import com.pojo.VIPCardUsage;

public interface VIPCardUsageService {
    VIPCardUsage detailByEnt(String openId);
    void purchaseVIPCard(PurchaseVIPCardDto purchaseVIPCardDto) throws Exception;
}
