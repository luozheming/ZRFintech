package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.pojo.City;
import com.service.manage.CityService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {
    @Autowired
    private CityService cityService;

    @GetMapping("/list")
    public String list(@RequestParam(name = "cityType", required = false) Integer cityType) {
        List<City> cityList = cityService.list(cityType);
        OutputFormate outputFormate = new OutputFormate(cityList, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    @PostMapping("/add")
    public String add(@RequestBody City city) {
        cityService.add(city);
        return ErrorCode.SUCCESS.toJsonString();
    }

    @PostMapping("/edit")
    public String edit(@RequestBody City city) {
        cityService.edit(city);
        return ErrorCode.SUCCESS.toJsonString();
    }
}
