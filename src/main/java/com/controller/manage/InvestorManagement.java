package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.pojo.Investor;
import com.service.manage.InvestorService;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class InvestorManagement {

    @Autowired
    InvestorService investorService;
    @Autowired
    CommonUtils commonUtils;
    @Value("photoSavedFilepath")
    StringBuilder photoSavedFilepath;
    @Value("cardSavedFilepath")
    StringBuilder cardSavedFilepath;
    @Value("orgphotoSavedFilepath")
    StringBuilder orgphotoSavedFilepath;

    @PostMapping("/investor/pageList")
    public String getInvestorPageList(int pageNum,int pageSize) {
        if (pageNum < 0 || pageSize <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        int totalPage = investorService.getDataCount()/pageSize;
        if(pageNum <= totalPage){
            List<Investor> investors = investorService.getInvestor(pageNum,pageSize);
            OutputFormate outputFormate = new OutputFormate(investors);
            return JSONObject.toJSONString(outputFormate);
        }
        return ErrorCode.PAGEBELLOWZERO.toJsonString();
    }

    @PostMapping("/investor/detail")
    public String getInvestorDetail(String investorId){
        Investor investor = investorService.getInvesById(investorId);
        OutputFormate outputFormate = new OutputFormate(investor);
        return JSONObject.toJSONString(outputFormate);
    }


    @PostMapping("/investor/add")
    public String addInvestor(@RequestPart(value = "photo", required = true) MultipartFile photo,
                              @RequestPart(value = "card", required = true) MultipartFile card,
                              @RequestPart(value = "orgphoto", required = false) MultipartFile orgphoto,
                              Investor investor) {
        String investId = commonUtils.getNumCode();
        String destPhotoPath  = photoSavedFilepath.append(investId).toString();
        String destCardPath  = cardSavedFilepath.append(investId).toString();
        try{
            commonUtils.uploadData(photo,destPhotoPath);
            commonUtils.uploadData(card,destCardPath);
        } catch (IllegalStateException | IOException e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        if(null!=orgphoto){
            try{
                String destOrgPath = orgphotoSavedFilepath.append(investId).toString();
                commonUtils.uploadData(orgphoto,destOrgPath);
                investor.setInvesOrgPhotoRoute(destOrgPath);
            }catch (IllegalStateException | IOException e) {
                return ErrorCode.OTHEREEEOR.toJsonString();
            }
        }
        investor.setInvestor(investId);
        investor.setInvesCardRoute(destCardPath);
        investor.setInvesPhotoRoute(destPhotoPath);
        investorService.addInvestor(investor);
        OutputFormate outputFormate = new OutputFormate(investId);
        return JSONObject.toJSONString(outputFormate);
    }


    @PostMapping("/investor/edit")
    public String editInvestor(Investor investor){
        investorService.editInvestor(investor);
        return ErrorCode.SUCCESS.toJsonString();
    }

    @PostMapping("/investor/status")
    public String status(String investorId, Integer status){
        investorService.status(investorId, status);
        return ErrorCode.SUCCESS.toJsonString();
    }
}
