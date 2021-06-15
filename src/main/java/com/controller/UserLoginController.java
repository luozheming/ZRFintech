package com.controller;


import com.alibaba.fastjson.JSONObject;
import com.dto.indto.EntUserLoginDto;
import com.dto.indto.EntUserRegisterDto;
import com.dto.indto.EntUserUpdatePasswordDto;
import com.dto.outdto.OutputFormate;
import com.pojo.EntUser;
import com.pojo.Investor;
import com.service.UserLoginService;
import com.service.VIPCardUsageService;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import com.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

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
    @Value("${entUserSavedFilepath}")
    StringBuilder entUserSavedFilepath;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private CommonUtils commonUtils;

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
                entUser.setCreateTime(new Date());
                entUser.setRoleCode("ent");
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

    /**
     * 客户标记需要联系客服
     * @param openId
     * @return
     */
    @PostMapping("/entuser/contactService")
    public String contactService(String openId, String phoneNm) {
        Update update = new Update();
        update.set("isContactService", true);
        if (!StringUtils.isEmpty(openId)) {
            mongoTemplate.updateFirst(query(where("openId").is(openId)), update, EntUser.class);
        } else {
            mongoTemplate.updateFirst(query(where("phoneNm").is(phoneNm)), update, EntUser.class);
        }

        return ErrorCode.SUCCESS.toJsonString();
    }


//    ---------------------------------项目一期PC版部分接口调整----------------------------------------------------

    /**
     * 用户注册
     * @param entUserRegisterDto
     * @return
     */
    @PostMapping("/entuser/register")
    public String register(@RequestBody EntUserRegisterDto entUserRegisterDto) {
        try {
            userLoginService.register(entUserRegisterDto);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            OutputFormate outputFormate = new OutputFormate(null, ErrorCode.OTHEREEEOR.getCode(), e.getMessage());
            return JSONObject.toJSONString(outputFormate);
        }
    }

    /**
     * 用户密码登录
     * @param entUserLoginDto
     * @return
     */
    @PostMapping("/entuser/login")
    public String login(@RequestBody EntUserLoginDto entUserLoginDto) {
        try {
            EntUser entUser = userLoginService.login(entUserLoginDto);
            if (null == entUser) {
                return ErrorCode.EMPITYUSER.toJsonString();
            }
            OutputFormate outputFormate = new OutputFormate(entUser, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            OutputFormate outputFormate = new OutputFormate(null, ErrorCode.OTHEREEEOR.getCode(), e.getMessage());
            return JSONObject.toJSONString(outputFormate);
        }
    }

    /**
     * 用户修改密码
     * @param entUserUpdatePasswordDto
     * @return
     */
    @PostMapping("/entuser/updatePassword")
    public String updatePassword(@RequestBody EntUserUpdatePasswordDto entUserUpdatePasswordDto) {
        try {
            userLoginService.updatePassword(entUserUpdatePasswordDto);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            OutputFormate outputFormate = new OutputFormate(null, ErrorCode.OTHEREEEOR.getCode(), e.getMessage());
            return JSONObject.toJSONString(outputFormate);
        }
    }

    /**
     * 修改用户
     * @param photoFile
     * @param entUser
     * @return
     */
    @PostMapping("/entuser/edit")
    public String edit(MultipartFile photoFile, EntUser entUser) {
        try {
            String destPhotoPath = entUserSavedFilepath.toString();
            if (null != photoFile) {
                commonUtils.uploadData(photoFile, destPhotoPath);
                entUser.setPhotoRoute(destPhotoPath + "/" + photoFile.getOriginalFilename());
            }
            userLoginService.edit(entUser);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 查询用户详情
     * @param userId
     * @return
     */
    @GetMapping("/entuser/detail")
    public String detail(@RequestParam String userId) {
        EntUser entUser = userLoginService.detail(userId);
        OutputFormate outputFormate = new OutputFormate(entUser, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     * 通过userId查询投资人信息
     * @param userId
     * @return
     */
    @GetMapping("/investor/investorByUserId")
    public String investorByUserId(String userId) {
        Investor investor = userLoginService.investorById(userId);
        OutputFormate outputFormate = new OutputFormate(investor, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     * 发送短信验证码
     * @param smsType
     * @param phoneNm
     * @return
     */
    @PostMapping("/entUser/sendSms")
    public String sendSms(Integer smsType, String phoneNm) {
        try {
            userLoginService.sendSms(smsType, phoneNm);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }
}
