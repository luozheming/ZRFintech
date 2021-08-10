package com.service;

import com.pojo.VIPCardUsageLog;

import java.util.List;

public interface VIPCardUsageLogService {
    List<VIPCardUsageLog> list(String vipCardUsageId);
    void add(VIPCardUsageLog vipCardUsageLog) throws Exception;
    void sendMailToAdviser(String userId, String telephoneNo) throws Exception;
}
