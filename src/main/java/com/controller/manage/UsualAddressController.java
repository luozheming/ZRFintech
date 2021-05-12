package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.pojo.UsualAddress;
import com.service.manage.UsualAddressService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usualAddress")
public class UsualAddressController {
    @Autowired
    private UsualAddressService usualAddressService;

    @GetMapping("/listByUserId")
    public String listByUserId(@RequestParam String userId) {
        List<UsualAddress> usualAddressList = usualAddressService.listByUserId(userId);
        OutputFormate outputFormate = new OutputFormate(usualAddressList, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    @PostMapping("/add")
    public String add(@RequestBody UsualAddress usualAddress) {
        usualAddressService.add(usualAddress);
        return ErrorCode.SUCCESS.toJsonString();
    }

    @PostMapping("/edit")
    public String edit(@RequestBody UsualAddress usualAddress) {
        usualAddressService.edit(usualAddress);
        return ErrorCode.SUCCESS.toJsonString();
    }

    @DeleteMapping("/delete")
    public String delete(String id) {
        usualAddressService.delete(id);
        return ErrorCode.SUCCESS.toJsonString();
    }
}
