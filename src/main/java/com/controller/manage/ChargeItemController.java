package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.pojo.ChargeItem;
import com.service.manage.ChargeItemService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chargeItem")
public class ChargeItemController {

    @Autowired
    private ChargeItemService chargeItemService;

    /**
     * 获取收费项目列表
     * @return
     */
    @GetMapping("/list")
    public String list() {
        try {
            List<ChargeItem> chargeItems = chargeItemService.list();// 暂时只有一种卡
            OutputFormate outputFormate = new OutputFormate(chargeItems);
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 修改收费项目信息
     * @param chargeItem
     * @return
     */
    @PostMapping("/edit")
    public String edit(@RequestBody ChargeItem chargeItem) {
        chargeItemService.update(chargeItem);
        return ErrorCode.SUCCESS.toJsonString();
    }

    /**
     * 新增收费项目
     * @param chargeItem
     * @return
     */
    @PostMapping("/add")
    public String add(@RequestBody ChargeItem chargeItem) {
        chargeItemService.insert(chargeItem);
        return ErrorCode.SUCCESS.toJsonString();
    }

}
