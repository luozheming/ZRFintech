package com.service;

import com.dto.outdto.VIPCardUsageRespDto;
import com.pojo.VIPCardUsage;

public interface VIPCardUsageService {
    VIPCardUsageRespDto detailByUserId(String userId);
    void add(VIPCardUsage vipCardUsage) throws Exception;
    void edit(VIPCardUsage vipCardUsage);
}
