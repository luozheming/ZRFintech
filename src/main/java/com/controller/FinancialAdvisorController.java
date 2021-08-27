package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.indto.PageDto;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.pojo.Attention;
import com.pojo.FinancialAdvisor;
import com.pojo.User;
import com.service.FinancialAdvisorService;
import com.service.manage.UserService;
import com.utils.ErrorCode;
import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/financialAdvisor")
public class FinancialAdvisorController {

    @Autowired
    private FinancialAdvisorService financialAdvisorService;
    @Autowired
    private UserService userService;

    /**
     * FA列表，含个人、机构FA（后台管理、小程序共用一个接口）
     * @param pageDto
     * @return
     */
    @GetMapping("/pageList")
    public String pageList(PageDto pageDto) {
        if (pageDto.getPageNum() < 0 || pageDto.getPageSize() <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        PageListDto pageListDto = financialAdvisorService.pageList(pageDto);
        if (null != pageListDto && !CollectionUtils.isEmpty(pageListDto.getList())) {
            List<FinancialAdvisor> financialAdvisorList = pageListDto.getList();
            for (FinancialAdvisor financialAdvisor : financialAdvisorList) {
                if (!StringUtils.isEmpty(financialAdvisor.getOrgNm()) && !StringUtils.isEmpty(financialAdvisor.getPositionName())) {
                    financialAdvisor.setOrgNm(financialAdvisor.getOrgNm() + "  |  " + financialAdvisor.getPositionName());
                }
//                User user = userService.getById(financialAdvisor.getFaId());
//                if (null != user) {
//                   financialAdvisor.setRoleCode(user.getRoleCode());
//                }
            }
        }
        OutputFormate outputFormate = new OutputFormate(pageListDto, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    @PostMapping("/add")
    public String add(@RequestBody FinancialAdvisor financialAdvisor) {
        try {
            financialAdvisorService.add(financialAdvisor);
            return ErrorCode.SUCCESS.toJsonString();
        }catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @PostMapping("/edit")
    public String edit(@RequestBody FinancialAdvisor financialAdvisor) {
        try {
            financialAdvisorService.edit(financialAdvisor);
            return ErrorCode.SUCCESS.toJsonString();
        }catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @PostMapping("/status")
    public String status(@RequestBody FinancialAdvisor financialAdvisor) {
        try {
            financialAdvisorService.status(financialAdvisor);
            return ErrorCode.SUCCESS.toJsonString();
        }catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @DeleteMapping("/delete")
    public String delete(String faId) {
        try {
            financialAdvisorService.delete(faId);
            return ErrorCode.SUCCESS.toJsonString();
        }catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @GetMapping("/detail")
    public String detail(String faId) {
        try {
            FinancialAdvisor financialAdvisor = financialAdvisorService.detail(faId);
            OutputFormate outputFormate = new OutputFormate(financialAdvisor, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
            return JSONObject.toJSONString(outputFormate);
        }catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }
}
