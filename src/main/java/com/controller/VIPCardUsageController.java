package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.VIPCardUsageRespDto;
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
     * @param userId
     * @return
     */
    @GetMapping("/detailByUserId")
    public String detailByUserId(@RequestParam String userId) {
        try {
            VIPCardUsageRespDto vipCardUsageRespDto = vipCardUsageService.detailByUserId(userId);
            OutputFormate outputFormate = new OutputFormate(vipCardUsageRespDto);
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 购买会员卡
     * @param vipCardUsage
     * @return
     */
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

}
