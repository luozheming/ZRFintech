package com.service;

import com.pojo.VIPCardUsage;

public interface VIPCardUsageService {
    VIPCardUsage detailByEnt(String openId);
    void add(VIPCardUsage vipCardUsage) throws Exception;
    void edit(VIPCardUsage vipCardUsage);
    void clearTimes(String openId);
}
