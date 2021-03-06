package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.indto.InvestorPageListDto;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.pojo.Investor;
import com.service.manage.InvestorService;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
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
    @Value("${photoSavedFilepath}")
    StringBuilder photoSavedFilepath;
    @Value("${cardSavedFilepath}")
    StringBuilder cardSavedFilepath;
    @Value("${orgphotoSavedFilepath}")
    StringBuilder orgphotoSavedFilepath;
    @Value("${s3BucketName}")
    private String s3BucketName;

    @GetMapping("/investor/pageList")
    public String getInvestorPageList(InvestorPageListDto investorPageListDto) {
        if (investorPageListDto.getPageNum() < 0 || investorPageListDto.getPageSize() <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        int count = investorService.getDataCount();
        int totalPage = count/investorPageListDto.getPageSize();
        if(investorPageListDto.getPageNum() <= totalPage){
            PageListDto pageListDto = new PageListDto<Investor>();
            List<Investor> investors = investorService.getInvestor(investorPageListDto.getPageNum(), investorPageListDto.getPageSize());
            if (!CollectionUtils.isEmpty(investors)) {
                for (Investor investor : investors) {
                    if (!StringUtils.isEmpty(investor.getInvesPhotoRoute())) {
                        investor.setInvesPhotoRoute(commonUtils.getFullFilePath(investor.getInvesPhotoRoute()));
                    }
                }
            }
            pageListDto.setTotal(count);
            pageListDto.setList(investors);
            OutputFormate outputFormate = new OutputFormate(pageListDto);
            return JSONObject.toJSONString(outputFormate);
        }
        return ErrorCode.PAGEBELLOWZERO.toJsonString();
    }

    @GetMapping("/investor/detail")
    public String getInvestorDetail(String investorId){
        Investor investor = investorService.getInvesById(investorId);
        OutputFormate outputFormate = new OutputFormate(investor);
        return JSONObject.toJSONString(outputFormate);
    }

    @PostMapping("/investor/add")
    public String addInvestor(@RequestPart(value = "photoFile", required = true) MultipartFile photoFile,
                              @RequestPart(value = "cardFile", required = false) MultipartFile cardFile,
                              @RequestPart(value = "orgphotoFile", required = true) MultipartFile orgphotoFile,
                              Investor investor) {
        String investId = commonUtils.getNumCode();
        String destPhotoPath  = photoSavedFilepath.toString();
        String destCardPath  = cardSavedFilepath.toString();
        try{
            // AWS S3????????????
            commonUtils.uploadFile(s3BucketName,destPhotoPath + photoFile.getOriginalFilename(), photoFile.getBytes());
            commonUtils.uploadFile(s3BucketName,destPhotoPath + cardFile.getOriginalFilename(), cardFile.getBytes());
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        if(null!=orgphotoFile){
            try{
                String destOrgPath = orgphotoSavedFilepath.toString();
                // AWS S3????????????
                commonUtils.uploadFile(s3BucketName,destOrgPath + orgphotoFile.getOriginalFilename(), orgphotoFile.getBytes());
                investor.setInvesOrgPhotoRoute(destOrgPath + orgphotoFile.getOriginalFilename());
            }catch (Exception e) {
                return ErrorCode.OTHEREEEOR.toJsonString();
            }
        }
        investor.setInvestorId(investId);
        investor.setStatus(0);
        if (null != cardFile) {
            investor.setInvesCardRoute(destCardPath + cardFile.getOriginalFilename());
        }
        investor.setInvesPhotoRoute(destPhotoPath + photoFile.getOriginalFilename());
        investorService.addInvestor(investor);
        OutputFormate outputFormate = new OutputFormate(investId);
        return JSONObject.toJSONString(outputFormate);
    }


    @PostMapping("/investor/edit")
    public String editInvestor(@RequestPart(value = "photoFile", required = false) MultipartFile photoFile,
                               @RequestPart(value = "cardFile", required = false) MultipartFile cardFile,
                               @RequestPart(value = "orgphotoFile", required = false) MultipartFile orgphotoFile,
                               Investor investor) {
        String destPhotoPath  = photoSavedFilepath.toString();
        String destCardPath  = cardSavedFilepath.toString();
        String destOrgPath = orgphotoSavedFilepath.toString();
        try{
            if (null != photoFile) {
                // AWS S3????????????
                commonUtils.uploadFile(s3BucketName,destPhotoPath + photoFile.getOriginalFilename(), photoFile.getBytes());
                investor.setInvesPhotoRoute(destPhotoPath + photoFile.getOriginalFilename());
            }
            if (null != cardFile) {
                // AWS S3????????????
                commonUtils.uploadFile(s3BucketName,destCardPath + cardFile.getOriginalFilename(), cardFile.getBytes());
                investor.setInvesCardRoute(destCardPath + cardFile.getOriginalFilename());
            }
            if (null != orgphotoFile) {
                // AWS S3????????????
                commonUtils.uploadFile(s3BucketName,destOrgPath + orgphotoFile.getOriginalFilename(), orgphotoFile.getBytes());
                investor.setInvesOrgPhotoRoute(destOrgPath + orgphotoFile.getOriginalFilename());
            }
            investorService.editInvestor(investor);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @PostMapping("/investor/status")
    public String status(String investorId, Integer status){
        investorService.status(investorId, status);
        return ErrorCode.SUCCESS.toJsonString();
    }

    @PostMapping("/investor/updateShowFlag")
    public String updateShowFlag(String investorId, Integer showFlag){
        investorService.updateShowFlag(investorId, showFlag);
        return ErrorCode.SUCCESS.toJsonString();
    }

}
