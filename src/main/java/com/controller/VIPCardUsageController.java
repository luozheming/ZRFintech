package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.pojo.VIPCardUsage;
import com.service.VIPCardUsageService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vipCardUsage")
public class VIPCardUsageController {

    @Autowired
    private VIPCardUsageService vipCardUsageService;

    /**
     * 查询用户VIP卡的使用情况
     * @param openId
     * @return
     */
    @GetMapping("/detailByEnt")
    public String detailByEnt(@RequestParam String openId) {
        try {
            VIPCardUsage vipCardUsage = vipCardUsageService.detailByEnt(openId);
            OutputFormate outputFormate = new OutputFormate(vipCardUsage);
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @PostMapping("/add")
    public String add(@RequestBody VIPCardUsage vipCardUsage) {
        try {
            vipCardUsageService.add(vipCardUsage);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @PostMapping("/edit")
    public String edit(@RequestBody VIPCardUsage vipCardUsage) {
        try {
            vipCardUsageService.edit(vipCardUsage);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @PostMapping("/clearTimes")
    public String clearTimes(String openId) {
        try {
            vipCardUsageService.clearTimes(openId);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }
}
