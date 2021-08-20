package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.pojo.VIPCard;
import com.service.VIPCardService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vipCard")
public class VIPCardController {

    @Autowired
    private VIPCardService vipCardService;

    /**
     * 获取金卡列表
     * @return
     */
    @GetMapping("/list")
    public String list() {
        try {
            List<VIPCard> vipCards = vipCardService.list();// 暂时只有一种卡
            OutputFormate outputFormate = new OutputFormate(vipCards);
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 修改金卡信息
     * @param vipCard
     * @return
     */
    @PostMapping("/edit")
    public String edit(VIPCard vipCard) {
        vipCardService.update(vipCard);
        return ErrorCode.SUCCESS.toJsonString();
    }

}
