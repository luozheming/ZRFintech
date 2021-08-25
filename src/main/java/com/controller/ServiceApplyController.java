package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.indto.PageDto;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.pojo.ServiceApply;
import com.service.ServiceApplyService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/serviceApply")
public class ServiceApplyController {

    @Autowired
    private ServiceApplyService serviceApplyService;

    @GetMapping("/pageList")
    public String pageList(PageDto pageDto) {
        if (pageDto.getPageNum() < 0 || pageDto.getPageSize() <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        PageListDto pageListDto = serviceApplyService.pageList(pageDto);
        OutputFormate outputFormate = new OutputFormate(pageListDto);
        return JSONObject.toJSONString(outputFormate);
    }

    @PostMapping("/add")
    public String add(@RequestBody ServiceApply serviceApply) {
        try{
            serviceApplyService.add(serviceApply);
            serviceApplyService.sendMail(serviceApply);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e){
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }
}
