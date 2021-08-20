package com.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dto.indto.UserEditDto;
import com.dto.indto.UserRegisterDto;
import com.dto.indto.UserUpdatePasswordDto;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.UserLoginDto;
import com.dto.outdto.UserRespDto;
import com.enums.RoleCode;
import com.pojo.*;
import com.service.UserLoginService;
import com.service.VIPCardUsageService;
import com.service.manage.UserService;
import com.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    @Value("${s3BucketName}")
    private String s3BucketName;
    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserLoginController.class);

    @PostMapping(value = "/investor/investorLogin")
    public String investorLogin(@RequestBody Investor investor){
        try{
            String phoneNm = investor.getPhoneNm();
            Investor findInvestor = mongoTemplate.findOne(query(where("phoneNm").is(phoneNm).and("status").is(0)),Investor.class);
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

    @PostMapping(value = "/user/loginByWechat")
    public String loginByWechat(@RequestBody User user) {
        logger.info("用户小程序登录入参：" + JSONObject.toJSONString(user));
        UserRespDto userRespDto = new UserRespDto();
        try{
            //如果用户已存在数据库，返回成功信息。否则将用户数据保存至数据库
            User userResp = mongoTemplate.findOne(query(where("openId").is(user.getOpenId()).and("isDelete").is(0)), User.class);
            if(!StringUtils.isEmpty(userResp)){
                BeanUtils.copyProperties(userResp, userRespDto);
                if (RoleCode.INVESTOR.getCode().equals(userResp.getRoleCode())) {
                    // 获取投资人用户额外信息
                    Investor investor = mongoTemplate.findOne(query(where("investorId").is(userResp.getUserId())), Investor.class);
                    userRespDto.setExtendData(investor);
                } else if (RoleCode.ENTUSER.getCode().equals(userResp.getRoleCode())) {
                    // 获取企业客户用户额外信息
                    EntUser entUser = mongoTemplate.findOne(query(where("entUserId").is(userResp.getUserId())), EntUser.class);
                    userRespDto.setExtendData(entUser);
                } else if (RoleCode.FINANCIALADVISOR.getCode().equals(userResp.getRoleCode())) {
                    // 获取FA用户额外信息
                    FinancialAdvisor financialAdvisor = mongoTemplate.findOne(query(where("faId").is(userResp.getUserId())), FinancialAdvisor.class);
                    userRespDto.setExtendData(financialAdvisor);
                }
            } else {
                String userId = commonUtils.getNumCode();
                user.setUserId(userId);
                user.setIsVerify(false);
                user.setCreateTime(new Date());
                user.setIsDelete(0);// 是否注销：0-否，1-是
                user.setRoleCode(RoleCode.VISITOR.getCode());// 默认角色：visitor-游客
                mongoTemplate.insert(user);
                BeanUtils.copyProperties(user, userRespDto);
            }
            OutputFormate outputFormate = new OutputFormate(userRespDto, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
            return JSONObject.toJSONString(outputFormate);
        }catch (Exception e){
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 查看用户详情
     * @param userId
     * @return
     */
    @GetMapping("/user/userDetail")
    public String userDetail(@RequestParam String userId) {
        UserRespDto userRespDto = new UserRespDto();
        //如果用户已存在数据库，返回成功信息。否则将用户数据保存至数据库
        User userResp = mongoTemplate.findOne(query(where("userId").is(userId).and("isDelete").is(0)), User.class);
        if (!StringUtils.isEmpty(userResp)) {
            BeanUtils.copyProperties(userResp, userRespDto);
            if (RoleCode.INVESTOR.getCode().equals(userResp.getRoleCode())) {
                // 获取投资人用户额外信息
                Investor investor = mongoTemplate.findOne(query(where("investorId").is(userResp.getUserId())), Investor.class);
                userRespDto.setExtendData(investor);
            } else if (RoleCode.ENTUSER.getCode().equals(userResp.getRoleCode())) {
                // 获取企业客户用户额外信息
                EntUser entUser = mongoTemplate.findOne(query(where("entUserId").is(userResp.getUserId())), EntUser.class);
                userRespDto.setExtendData(entUser);
            } else if (RoleCode.FINANCIALADVISOR.getCode().equals(userResp.getRoleCode())) {
                // 获取FA用户额外信息
                FinancialAdvisor financialAdvisor = mongoTemplate.findOne(query(where("faId").is(userResp.getUserId())), FinancialAdvisor.class);
                userRespDto.setExtendData(financialAdvisor);
            }
        }
        OutputFormate outputFormate = new OutputFormate(userRespDto, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     * 企业客户登录接口（暂时不用）
     * @param entUser
     * @return
     */
    @PostMapping(value = "/entuser/entUserLogin")
    public String entUserLogin(@RequestBody EntUser entUser){
        try{
            //如果用户已存在数据库，返回成功信息。否则将用户数据保存至数据库
            EntUser entUserResp = mongoTemplate.findOne(query(where("openId").is(entUser.getOpenId())),EntUser.class);
            if(!StringUtils.isEmpty(entUserResp)){
                // 如果已入库的客户信息没有客户id则更新一个，该分支用于处理一期历史数据
                if (StringUtils.isEmpty(entUserResp.getEntUserId())) {
                    Update update = new Update();
                    String entUserId = commonUtils.getNumCode();
                    update.set("entUserId", entUserId);
                    mongoTemplate.updateFirst(query(where("openId").is(entUserResp.getOpenId())), update, EntUser.class);
                    entUserResp.setEntUserId(entUserId);

                    // 通过openId更新项目表Project的entUserId，方便后续查询时跟PC端同步用entUserId查询相关信息
                    Update projectUpdate = new Update();
                    projectUpdate.set("entUserId", entUserId);
                    mongoTemplate.updateMulti(query(where("openId").is(entUser.getOpenId())), update, Project.class);

                    // 通过openId更新评论表ProjectComment的entUserId，方便后续查询时跟PC端同步用entUserId查询相关信息
                    Update commentUpdate = new Update();
                    commentUpdate.set("entUserId", entUserId);
                    mongoTemplate.updateMulti(query(where("openId").is(entUser.getOpenId())), update, ProjectComment.class);
                }
                OutputFormate outputFormate = new OutputFormate(entUserResp, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
                return JSONObject.toJSONString(outputFormate);
            }else{
                String entUserId = commonUtils.getNumCode();
                // 通过手机号码获取基础用户信息
                User user = mongoTemplate.findOne(query(where("phoneNm").is(entUser.getPhoneNm()).and("status").is(0)), User.class);
                if (null != user) {
                    entUserId = user.getUserId();
                }
                entUser.setEntUserId(entUserId);// 客户id同用户基础表userId
//                entUser.setIsVerify(false);
                entUser.setCreateTime(new Date());
                mongoTemplate.insert(entUser);
                OutputFormate outputFormate = new OutputFormate(entUser, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
                return JSONObject.toJSONString(outputFormate);
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

    /**
     * 修改用户信息
     * @param userEditDto
     * @return
     */
    @PostMapping("/user/editUser")
    public String edit(MultipartFile photoFile, MultipartFile cardFile, MultipartFile cardBackFile, UserEditDto userEditDto) {
        logger.info("修改用户接口入参：" + JSONObject.toJSONString(userEditDto));
        try {
            if (!StringUtils.isEmpty(userEditDto.getCaptcha())) {
                Map<String, Object> respMap = userLoginService.validateSms(userEditDto.getPhoneNm(), userEditDto.getCaptcha());
                if (!(boolean)respMap.get("result")) {
                    OutputFormate outputFormate = new OutputFormate("", ErrorCode.OTHEREEEOR.getCode(), String.valueOf(respMap.get("msg")));
                    return JSONObject.toJSONString(outputFormate);
                }
            }

            Update update = new Update();
            String photoFilePath = "";
            if (null != photoFile) {
                photoFilePath = entUserSavedFilepath + userEditDto.getUserId() + "/" + photoFile.getOriginalFilename();
                // AWS S3存储文件
                commonUtils.uploadFile(s3BucketName, photoFilePath, photoFile.getBytes());
                //文件保存后更新数据库信息
                update.set("photoRoute", photoFilePath);
            }
            if (null != cardFile) {
                String filePath = entUserSavedFilepath + userEditDto.getUserId() + "/" + cardFile.getOriginalFilename();
                // AWS S3存储文件
                commonUtils.uploadFile(s3BucketName, filePath, cardFile.getBytes());
                //文件保存后更新数据库信息
                update.set("cardRoute", filePath);
            }
            if (null != cardBackFile) {
                String filePath = entUserSavedFilepath + userEditDto.getUserId() + "/" + cardBackFile.getOriginalFilename();
                // AWS S3存储文件
                commonUtils.uploadFile(s3BucketName, filePath, cardBackFile.getBytes());
                //文件保存后更新数据库信息
                update.set("cardBackRoute", filePath);
            }
            update.set("userName", userEditDto.getUserName());
            update.set("companyName", userEditDto.getCompanyName());
            update.set("positionName", userEditDto.getPositionName());
            update.set("email", userEditDto.getEmail());
            update.set("telephoneNo", userEditDto.getTelephoneNo());
            mongoTemplate.updateFirst(query(where("userId").is(userEditDto.getUserId())), update, User.class);

            // 如果用户是投资人，则一并修改投资人相关信息
            if (RoleCode.INVESTOR.getCode().equals(userEditDto.getRoleCode())) {
                Update investorUpdate = new Update();
                investorUpdate.set("focusFiled", userEditDto.getFocusFiled());
                investorUpdate.set("finRound", userEditDto.getFinRound());
                investorUpdate.set("selfIntroduction", userEditDto.getSelfIntroduction());
                investorUpdate.set("focusCity", userEditDto.getFocusCity());
                investorUpdate.set("invesEmail", userEditDto.getEmail());
                if (!StringUtils.isEmpty(photoFilePath)) {
                    investorUpdate.set("invesPhotoRoute", photoFilePath);
                }
                mongoTemplate.updateFirst(query(where("investorId").is(userEditDto.getUserId())), investorUpdate, Investor.class);
            } else if (RoleCode.FINANCIALADVISOR.getCode().equals(userEditDto.getRoleCode())) {
                Update faUpdate = new Update();
                faUpdate.set("focusFiled", userEditDto.getFocusFiled());
                faUpdate.set("finRound", userEditDto.getFinRound());
                faUpdate.set("selfIntroduction", userEditDto.getSelfIntroduction());
                faUpdate.set("city", userEditDto.getFocusCity());
                faUpdate.set("invesEmail", userEditDto.getEmail());
                if (!StringUtils.isEmpty(photoFilePath)) {
                    faUpdate.set("photoRoute", photoFilePath);
                }
                mongoTemplate.updateFirst(query(where("faId").is(userEditDto.getUserId())), faUpdate, FinancialAdvisor.class);
            }

            User userResp = mongoTemplate.findOne(query(where("userId").is(userEditDto.getUserId())), User.class);
            OutputFormate outputFormate = new OutputFormate(userResp, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            logger.error("修改客户信息系统异常：", e);
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 修改客户信息(暂时不用)
     * @param entUser
     * @return
     */
    @PostMapping("/entuser/edit")
    public String edit(EntUser entUser) {
        Update update = new Update();
//        update.set("companyName", entUser.getCompanyName());
//        update.set("positionName", entUser.getPositionName());
        mongoTemplate.updateFirst(query(where("entUserId").is(entUser.getEntUserId())), update, EntUser.class);
        return ErrorCode.SUCCESS.toJsonString();
    }

    /**
     * 企业客户认证
     * @param cardFile
     * @param user
     * @return
     */
    @PostMapping("/user/verify")
        public String verify(MultipartFile photoFile, MultipartFile cardFile, MultipartFile cardBackFile, User user, String captcha, String targetRoleCode) {
        logger.info("身份认证入参： user=" + JSONObject.toJSONString(user));
        logger.info("身份认证入参： targetRoleCode=" + targetRoleCode);
        Map<String, Object> respMap = userLoginService.validateSms(user.getPhoneNm(), captcha);
//        if (!(boolean)respMap.get("result")) {
//            OutputFormate outputFormate = new OutputFormate("", ErrorCode.OTHEREEEOR.getCode(), String.valueOf(respMap.get("msg")));
//            return JSONObject.toJSONString(outputFormate);
//        }
        try {
            User userResp = mongoTemplate.findOne(query(where("userId").is(user.getUserId())), User.class);
            logger.info("身份认证通过userId获取用户信息： userResp=" + JSONObject.toJSONString(userResp));
            if (null != userResp && userResp.getIsVerify()) {
                OutputFormate outputFormate = new OutputFormate("", ErrorCode.OTHEREEEOR.getCode(), "该用户已认证");
                return JSONObject.toJSONString(outputFormate);
            }
            Update update = new Update();
            if (null != photoFile) {
                String filePath = entUserSavedFilepath + user.getUserId() + "/" + photoFile.getOriginalFilename();
                // AWS S3存储文件
                commonUtils.uploadFile(s3BucketName, filePath, photoFile.getBytes());
                //文件保存后更新数据库信息
                update.set("photoRoute", filePath);
            }
            if (null != cardFile) {
                String filePath = entUserSavedFilepath + user.getUserId() + "/" + cardFile.getOriginalFilename();
                // AWS S3存储文件
                commonUtils.uploadFile(s3BucketName, filePath, cardFile.getBytes());
                //文件保存后更新数据库信息
                update.set("cardRoute", filePath);
            }
            if (null != cardBackFile) {
                String filePath = entUserSavedFilepath + user.getUserId() + "/" + cardBackFile.getOriginalFilename();
                // AWS S3存储文件
                commonUtils.uploadFile(s3BucketName, filePath, cardBackFile.getBytes());
                //文件保存后更新数据库信息
                update.set("cardBackRoute", filePath);
            }
            if (RoleCode.ENTUSER.getCode().equals(targetRoleCode)) {
                update.set("userName", user.getUserName());
                update.set("companyName", user.getCompanyName());
                update.set("positionName", user.getPositionName());
                update.set("email", user.getEmail());
                update.set("telephoneNo", user.getTelephoneNo());
                update.set("auditStatus", 2);
                update.set("roleCode", targetRoleCode);
                update.set("isVerify", true);

                EntUser entUser = mongoTemplate.findOne(query(where("entUserId").is(user.getUserId())), EntUser.class);
                if (null == entUser) {
                    entUser = new EntUser();
                    BeanUtils.copyProperties(user, entUser);
                    entUser.setEntUserId(user.getUserId());
                    mongoTemplate.save(entUser);
                }
            } else if (RoleCode.INVESTOR.getCode().equals(targetRoleCode)) {
                investorVerify(user, targetRoleCode, update);
            } else if (RoleCode.FINANCIALADVISOR.getCode().equals(targetRoleCode)) {
                financialAdvisorVerify(user, targetRoleCode, update);
            }
            // 更新用户
            mongoTemplate.updateFirst(query(where("userId").is(user.getUserId())), update, User.class);

            User userDto = mongoTemplate.findOne(query(where("userId").is(user.getUserId())), User.class);
            if (!StringUtils.isEmpty(userDto.getAuditStatus()) && 1 == userDto.getAuditStatus()) {
                // 申请认证为待审核状态的，异步发送邮件通知管理员审核
                userService.sendAuditMail(userDto);
            }
            OutputFormate outputFormate = new OutputFormate(userDto, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
            logger.info("身份认证成功...");
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            logger.error("身份认证系统异常：" + e);
            OutputFormate outputFormate = new OutputFormate("", ErrorCode.OTHEREEEOR.getCode(), e.getMessage());
            return JSONObject.toJSONString(outputFormate);
        }
    }

    /**
     * 认证投资人部分逻辑
     * @param user
     * @param targetRoleCode
     * @param update
     * @throws Exception
     */
    private void investorVerify(User user, String targetRoleCode, Update update) throws Exception {
        // 验证该投资人是否已维护
        Investor investor = mongoTemplate.findOne(query(where("phoneNm").is(user.getPhoneNm())), Investor.class);
        if (null == investor && StringUtils.isEmpty(user.getEmail())) {
            throw new Exception("请联系管理员注册或者上传资料审核验证");
        }

        // 如果为上传资料认证，则更新相关字段
        if (!StringUtils.isEmpty(user.getEmail())) {
            update.set("userName", user.getUserName());
            update.set("companyName", user.getCompanyName());
            update.set("positionName", user.getPositionName());
            update.set("email", user.getEmail());
            update.set("telephoneNo", user.getTelephoneNo());
            update.set("roleCode", targetRoleCode);
            update.set("auditRoleCode", targetRoleCode);
            update.set("isVerify", false);
            update.set("auditStatus", 1);// 审核状态：1-待审核，2-审核通过，3-审核不通过
        }

        if (null == investor) {
            // 通过机构名称和姓名查询是否存在系统导入的投资人信息
            Investor sysInvestor = mongoTemplate.findOne(query(where("investor").is(user.getUserName()).and("orgNm").is(user.getCompanyName() + "  |  " + user.getPhoneNm())), Investor.class);
            if (null != sysInvestor) {
                // 先删除原后台批量导入的信息
                mongoTemplate.remove(query(where("investorId").is(sysInvestor.getInvestorId())), Investor.class);
            }
            // 插入新的用户（投资人）额外信息
            Investor insertInvestor = new Investor();
            insertInvestor.setInvestorId(user.getUserId());
            insertInvestor.setInvestor(user.getUserName());
            insertInvestor.setInvesEmail(user.getEmail());
            insertInvestor.setStatus(1);// 状态：0-有效，1-失效
            insertInvestor.setShowFlag(0);// 投资人展示标识：0-不展示，1-投资人授权展示
            insertInvestor.setOrgNm(user.getCompanyName() + "  |  " + user.getPositionName());
            insertInvestor.setPhoneNm(user.getPhoneNm());
            if (StringUtils.isEmpty(user.getPhotoRoute())) {
                // 给个随机默认投资人头像
                String photoRoute = "/home/ec2-user/data/investor/";
                int random = new Random().nextInt(13) + 1;
                String fileName = random + ".jpg";
                photoRoute = photoRoute + fileName;
                insertInvestor.setInvesPhotoRoute(photoRoute);

                update.set("photoRoute", photoRoute);// 更新用户的头像
            }
            mongoTemplate.insert(insertInvestor, "investor");
        } else {
            update.set("auditStatus", 2);// 审核状态：1-待审核，2-审核通过，3-审核不通过
            update.set("isVerify", true);
            Update investorUpdate = new Update();
            investorUpdate.set("investorId", user.getUserId());
            if (!StringUtils.isEmpty(user.getEmail())) {
                investorUpdate.set("investor", user.getUserName());
                investorUpdate.set("invesEmail", user.getEmail());
                investorUpdate.set("status", 0);// 状态：0-有效，1-失效
                investorUpdate.set("orgNm", user.getCompanyName() + "  |  " + user.getPositionName());
                investorUpdate.set("phoneNm", user.getPhoneNm());
            } else {
                update.set("userName", investor.getInvestor());
                if (!StringUtils.isEmpty(investor.getOrgNm()) && investor.getOrgNm().split("\\|").length == 2) {
                    update.set("companyName", investor.getOrgNm().split("\\|")[0].trim());
                    update.set("positionName", investor.getOrgNm().split("\\|")[1].trim());
                }
                update.set("email", investor.getInvesEmail());
                update.set("telephoneNo", investor.getPhoneNm());
            }
            mongoTemplate.updateFirst(query(where("investorId").is(investor.getInvestorId())), investorUpdate, Investor.class);
        }
    }

    /**
     * 认证投FA部分逻辑
     * @param user
     * @param targetRoleCode
     * @param update
     * @throws Exception
     */
    private void financialAdvisorVerify(User user, String targetRoleCode, Update update) throws Exception {
        // 验证该fa是否已维护
        FinancialAdvisor financialAdvisor = mongoTemplate.findOne(query(where("phoneNm").is(user.getPhoneNm())), FinancialAdvisor.class);
        if (null == financialAdvisor && StringUtils.isEmpty(user.getEmail())) {
            throw new Exception("请联系管理员注册或者上传资料审核验证");
        }

        // 如果为上传资料认证，则更新相关字段
        if (!StringUtils.isEmpty(user.getEmail())) {
            update.set("userName", user.getUserName());
            update.set("companyName", user.getCompanyName());
            update.set("positionName", user.getPositionName());
            update.set("email", user.getEmail());
            update.set("telephoneNo", user.getTelephoneNo());
            update.set("roleCode", targetRoleCode);
            update.set("auditRoleCode", targetRoleCode);
            update.set("isVerify", false);
            update.set("auditStatus", 1);// 审核状态：1-待审核，2-审核通过，3-审核不通过
        }

        if (null == financialAdvisor) {
            // 通过机构名称和姓名查询是否存在系统导入的投资人信息
            FinancialAdvisor sysFA = mongoTemplate.findOne(query(where("faName").is(user.getUserName()).and("orgNm").is(user.getCompanyName())), FinancialAdvisor.class);
            if (null != sysFA) {
                // 先删除系统批量导入的FA信息
                mongoTemplate.remove(query(where("faId").is(sysFA.getFaId())), FinancialAdvisor.class);
            }
            // 插入新的用户（投资人）额外信息
            FinancialAdvisor fa = new FinancialAdvisor();
            fa.setFaId(user.getUserId());
            fa.setFaName(user.getUserName());
            fa.setEmail(user.getEmail());
            fa.setStatus(1);//状态：0-有效，1-失效
            fa.setOrgNm(user.getCompanyName());
            fa.setPhoneNm(user.getPhoneNm());
            mongoTemplate.insert(fa, "financialAdvisor");
        } else {
            update.set("auditStatus", 2);// 审核状态：1-待审核，2-审核通过，3-审核不通过
            update.set("isVerify", true);
            if (!StringUtils.isEmpty(user.getEmail())) {
                Update faUpdate = new Update();
                faUpdate.set("faId", user.getUserId());
                faUpdate.set("faName", user.getUserName());
                faUpdate.set("email", user.getEmail());
                faUpdate.set("status", 0);// 状态：0-有效，1-失效
                faUpdate.set("orgNm", user.getCompanyName());
                faUpdate.set("phoneNm", user.getPhoneNm());
                faUpdate.set("faType", 1);
                mongoTemplate.updateFirst(query(where("faId").is(financialAdvisor.getFaId())), faUpdate, Investor.class);
            } else {
                update.set("userName", financialAdvisor.getFaName());
                update.set("companyName", financialAdvisor.getOrgNm());
                update.set("positionName", financialAdvisor.getPositionName());
                update.set("email", financialAdvisor.getEmail());
                update.set("telephoneNo", financialAdvisor.getPhoneNm());
            }
        }
    }


    /**
     * 手机验证码校验及手机号码授权
     * @param entUser
     * @param captcha
     * @return
     */
    @PostMapping("/entuser/phoneNmVerify")
    public String verify(EntUser entUser, String captcha, Boolean isPhoneNmChange) {
        try {
            Map<String, Object> respMap = userLoginService.validateSms(entUser.getPhoneNm(), captcha);
            if (!(boolean)respMap.get("result")) {
                OutputFormate outputFormate = new OutputFormate("", ErrorCode.OTHEREEEOR.getCode(), String.valueOf(respMap.get("msg")));
                return JSONObject.toJSONString(outputFormate);
            }
            if (null != isPhoneNmChange && isPhoneNmChange) {
                Update update = new Update();
                update.set("phoneNm", entUser.getPhoneNm());
                mongoTemplate.updateFirst(query(where("entUserId").is(entUser.getEntUserId())), update, EntUser.class);
            }
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @PostMapping("/user/chooseRole")
    public String chooseRole(String userId, String targetRoleCode) {
        Update update = new Update();
        update.set("roleCode", targetRoleCode);
        mongoTemplate.updateFirst(query(where("userId").is(userId)), update, User.class);
        User userDto = mongoTemplate.findOne(query(where("userId").is(userId)), User.class);
        OutputFormate outputFormate = new OutputFormate(userDto, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }



//    ---------------------------------项目一期PC版部分接口调整----------------------------------------------------

    /**
     * 用户注册
     * @param userRegisterDto
     * @return
     */
    @PostMapping("/user/register")
    public String register(@RequestBody UserRegisterDto userRegisterDto) {
        try {
            userLoginService.register(userRegisterDto);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            OutputFormate outputFormate = new OutputFormate(null, ErrorCode.OTHEREEEOR.getCode(), e.getMessage());
            return JSONObject.toJSONString(outputFormate);
        }
    }

    /**
     * 用户密码登录
     * @param user
     * @return
     */
    @PostMapping("/user/loginByPassword")
    public String loginByPassword(@RequestBody User user) {
        try {
            UserLoginDto userLoginDto = userLoginService.loginByPassword(user);
            if (null == userLoginDto) {
                return ErrorCode.EMPITYUSER.toJsonString();
            }
            OutputFormate outputFormate = new OutputFormate(userLoginDto, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            OutputFormate outputFormate = new OutputFormate(null, ErrorCode.OTHEREEEOR.getCode(), e.getMessage());
            return JSONObject.toJSONString(outputFormate);
        }
    }

    /**
     * 用户修改密码
     * @param userUpdatePasswordDto
     * @return
     */
    @PostMapping("/user/updatePassword")
    public String updatePassword(@RequestBody UserUpdatePasswordDto userUpdatePasswordDto) {
        try {
            userLoginService.updatePassword(userUpdatePasswordDto);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            OutputFormate outputFormate = new OutputFormate(null, ErrorCode.OTHEREEEOR.getCode(), e.getMessage());
            return JSONObject.toJSONString(outputFormate);
        }
    }

    /**
     * 查询用户详情
     * @param userId
     * @return
     */
    @GetMapping("/user/detail")
    public String detail(String userId) {
        User user = userLoginService.detail(userId);
        OutputFormate outputFormate = new OutputFormate(user, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
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
            System.out.println(e.getMessage());
            System.out.println(e);
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 获取s3路径
     * @param bucketName,objectKey
     * @return
     */
    @GetMapping("/getUrl")
    public String getUrl(String bucketName, String objectKey) {
        try {
            String url = GeneratePresignedURLUtil.GeneratePresignedURL(bucketName, objectKey);
            OutputFormate outputFormate = new OutputFormate(url, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     *
     * @param file
     * @param bucketName
     * @param objectKey
     * @return
     */
    @PostMapping("/s3/uploadFile")
    public String uploadFile(MultipartFile file, String bucketName, String objectKey) {
        try {
            byte[] fileData = file.getBytes();
            String str = S3Util.uploadFile(bucketName, objectKey, fileData);
            return JSONObject.toJSONString(str);
        } catch (Exception e) {
            e.getMessage();
            return e.getMessage();
        }
    }




//    ----------------------公众号测试代码--------------------
    private static String getToken() throws Exception {
        // access_token接口https请求方式: GET https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
        String path = " https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
        String appid = "wx721ab3d889e118c7";
        String secret = "dae62109f0aa9d6501ab5469ab6a4bc4";
        URL url = new URL(path+"&appid=" + appid + "&secret=" + secret);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        InputStream in = connection.getInputStream();
        byte[] b = new byte[100];
        int len = -1;
        StringBuffer sb = new StringBuffer();
        while((len = in.read(b)) != -1) {
            sb.append(new String(b,0,len));
        }

        System.out.println(sb.toString());
        in.close();
        return sb.toString();
    }

    private static String getContentList(String token) throws IOException {
        String path = " https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=" + token;
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("content-type", "application/json;charset=utf-8");
        connection.connect();
        // post发送的参数
        Map<String, Object> map = new HashMap<>();
        map.put("type", "news"); // news表示图文类型的素材，具体看API文档
        map.put("offset", 0);
        map.put("count", 1);
        // 将map转换成json字符串
        String paramBody = JSON.toJSONString(map); // 这里用了Alibaba的fastjson

        OutputStream out = connection.getOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
        bw.write(paramBody); // 向流中写入参数字符串
        bw.flush();

        InputStream in = connection.getInputStream();
        byte[] b = new byte[100];
        int len = -1;
        StringBuffer sb = new StringBuffer();
        while((len = in.read(b)) != -1) {
            sb.append(new String(b,0,len));
        }

        in.close();
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {

        String result1 = getToken();
        Map<String,Object> token = (Map<String, Object>) JSON.parseObject(result1);
        String result2 = getContentList(token.get("access_token").toString());
        System.out.println(result2);
    }

}
