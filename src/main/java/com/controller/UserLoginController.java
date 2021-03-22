package com.controller;


import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.InvestorCommentAmountDto;
import com.dto.outdto.OutputFormate;
import com.pojo.EntUser;
import com.pojo.Investor;
import com.pojo.ProjectComment;
import com.utils.ErrorCode;
import org.bson.internal.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class UserLoginController {
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping(value = "/investorLogin")
    public String investorLogin(@RequestBody Investor investor){
        try{
            String phoneNm = investor.getPhoneNm();
            Investor findInvestor = mongoTemplate.findOne(query(where("phoneNm").is(phoneNm)),Investor.class);
            if(!StringUtils.isEmpty(findInvestor)){
                // 获取投资人及机构图片信息
                findInvestor.setPhoto(getPhoto(findInvestor.getInvesPhotoRoute()));
                findInvestor.setOrgPhoto(getPhoto(findInvestor.getInvesOrgPhotoRoute()));

                // 获取投资人评论的金额总计信息
                InvestorCommentAmountDto investorCommentAmountDto = getCommentAmount(findInvestor.getInvestorId());
                findInvestor.setAccomplishedAmount(investorCommentAmountDto.getAccomplishedAmount());
                findInvestor.setUnaccomplishedAmount(investorCommentAmountDto.getUnaccomplishedAmount());

                OutputFormate outputFormate = new OutputFormate(findInvestor);
                return JSONObject.toJSONString(outputFormate);
            }else{
                return ErrorCode.NULLOBJECT.toJsonString();
            }
        }catch (Exception e){
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @PostMapping(value = "/entUserLogin")
    public String entUserLogin(@RequestBody EntUser entUser){
        try{
        //如果用户已存在数据库，返回成功信息。否则将用户数据保存至数据库
        if(!StringUtils.isEmpty(mongoTemplate.findOne(query(where("openId").is(entUser.getOpenId())),EntUser.class))){
            return ErrorCode.SUCCESS.toJsonString();
        }else{
            mongoTemplate.insert(entUser);
            return ErrorCode.USERFIRSTLOGIN.toJsonString();
        }
    }catch (Exception e){
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 获取图片信息
     * @param filePath
     * @return
     */
    public String getPhoto(String filePath) {
        String photo = "";
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream bos = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int len = -1;
            while((len = fileInputStream.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            byte[] fileByte = bos.toByteArray();
            //进行base64位加密
            photo = new String(Base64.encode(fileByte));
        } catch (Exception e) {
            return "";
        } finally {
            try {
                if (null != fileInputStream) {
                    fileInputStream.close();
                }
                if (null != bos) {
                    bos.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return photo;
    }

    /**
     * 获取
     * @param investorId
     * @return
     */
    public InvestorCommentAmountDto getCommentAmount(String investorId) {
        InvestorCommentAmountDto investorCommentAmountDto = new InvestorCommentAmountDto();
        List<ProjectComment> projectComments = mongoTemplate.find(query(Criteria.where("investorId").is(investorId)), ProjectComment.class);
        if (!CollectionUtils.isEmpty(projectComments)) {
            BigDecimal unaccomplishedAmount = new BigDecimal("0.00");
            BigDecimal accomplishedAmount = new BigDecimal("0.00");
            BigDecimal commentAmount = null;
            for (ProjectComment projectComment : projectComments) {
                // 根据是否完成：true-完成，false-未完成，判断评论金额是否获取
                commentAmount = projectComment.getCommentAmount() == null ? new BigDecimal("0.00") : projectComment.getCommentAmount();
                if (projectComment.getIsDone()) {
                    accomplishedAmount = accomplishedAmount.add(commentAmount);// 已获取金额
                } else {
                    unaccomplishedAmount = unaccomplishedAmount.add(commentAmount);// 未获取金额
                }
            }
            investorCommentAmountDto.setInvestorId(investorId);
            investorCommentAmountDto.setUnaccomplishedAmount(unaccomplishedAmount);
            investorCommentAmountDto.setAccomplishedAmount(accomplishedAmount);
        }
        return investorCommentAmountDto;
    }
}
