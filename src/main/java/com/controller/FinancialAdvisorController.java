package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.indto.PageDto;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.service.FinancialAdvisorService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/financialAdvisor")
public class FinancialAdvisorController {

    @Autowired
    private FinancialAdvisorService financialAdvisorService;

    @GetMapping("/pageList")
    public String pageList(PageDto pageDto) {
        if (pageDto.getPageNum() < 0 || pageDto.getPageSize() <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        PageListDto pageListDto = financialAdvisorService.pageList(pageDto);
        OutputFormate outputFormate = new OutputFormate(pageListDto, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }
}
