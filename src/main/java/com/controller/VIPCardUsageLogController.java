package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.pojo.VIPCardUsageLog;
import com.service.VIPCardUsageLogService;
import com.utils.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(VIPCardUsageController.class);

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
            OutputFormate outputFormate = new OutputFormate("", ErrorCode.OTHEREEEOR.getCode(), e.getMessage());
            return JSONObject.toJSONString(outputFormate);
        }
    }

    @PostMapping("/sendMailToAdviser")
    public String sendMailToAdviser(String userId, String telephoneNo) {
        try {
            vipCardUsageLogService.sendMailToAdviser(userId, telephoneNo);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            logger.error("金卡顾问服务申请通知系统错误:", e);
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }
}
