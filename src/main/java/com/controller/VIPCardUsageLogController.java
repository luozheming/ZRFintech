package com.controller;

import com.pojo.VIPCardUsageLog;
import com.service.VIPCardUsageLogService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vipCardUsageLog")
public class VIPCardUsageLogController {

    @Autowired
    private VIPCardUsageLogService vipCardUsageLogService;

    /**
     * 新增会员卡服务记录
     * @param vipCardUsageLog
     * @return
     */
    @PostMapping("/add")
    public String add(@RequestBody VIPCardUsageLog vipCardUsageLog) {
        try {
            vipCardUsageLogService.add(vipCardUsageLog);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

}
