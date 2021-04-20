package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.pojo.VIPCard;
import com.pojo.VIPCardUsage;
import com.service.VIPCardService;
import com.service.VIPCardUsageService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vipCard")
public class VIPCardController {

    @Autowired
    private VIPCardService vipCardService;

    @GetMapping("/detail")
    public String detail() {
        try {
            List<VIPCard> vipCards = vipCardService.list();// 暂时只有一种卡
            if (CollectionUtils.isEmpty(vipCards)) {
                return ErrorCode.NULLOBJECT.toJsonString();
            }
            OutputFormate outputFormate = new OutputFormate(vipCards.get(0));
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

}
