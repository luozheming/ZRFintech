package com.controller;


import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.pojo.EntUser;
import com.pojo.Investor;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RestController
public class UserLoginController {
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping(value = "/investor/investorLogin")
    public String investorLogin(@RequestBody Investor investor){
        try{
            String phoneNm = investor.getPhoneNm();
            Investor findInvestor = mongoTemplate.findOne(query(where("phoneNm").is(phoneNm)),Investor.class);
            if(!StringUtils.isEmpty(findInvestor)){
                OutputFormate outputFormate = new OutputFormate(findInvestor);
                return JSONObject.toJSONString(outputFormate);
            }else{
                return ErrorCode.NULLOBJECT.toJsonString();
            }
        }catch (Exception e){
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @PostMapping(value = "/entuser/entUserLogin")
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
}
