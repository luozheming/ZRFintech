package com.controller;


import com.alibaba.fastjson.JSONObject;
import com.dto.indto.PurchaseVIPCardDto;
import com.dto.outdto.OutputFormate;
import com.pojo.EntUser;
import com.pojo.Investor;
import com.service.VIPCardService;
import com.service.VIPCardUsageService;
import com.utils.ErrorCode;
import com.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RestController
public class UserLoginController {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private VIPCardUsageService vipCardUsageService;
    @Value("${wx.appId}")
    private String appId;
    @Value("${wx.secret}")
    private String secret;
    @Value("${wx.auth.code2Session}")
    private String code2SessionUrl;

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
            // 初始化VIP卡信息,后续购卡功能开放则去掉该逻辑
            PurchaseVIPCardDto purchaseVIPCardDto = new PurchaseVIPCardDto();
            purchaseVIPCardDto.setCardCount(1);
            purchaseVIPCardDto.setOpenId(entUser.getOpenId());
            vipCardUsageService.purchaseVIPCard(purchaseVIPCardDto);

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
     * 微信登录
     * @param js_code
     * @return
     */
    @GetMapping("/wx/getCode2Session")
    public String getCode2Session(@RequestParam(value = "js_code", required = true) String js_code) {
        try {
            String url = code2SessionUrl + "?appId=" + appId + "&secret=" + secret
                    + "&js_code=" + js_code + "&grant_type=authorization_code";
            String resp = HttpClientUtil.doGet(url);
            OutputFormate outputFormate = new OutputFormate(resp, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }
}
