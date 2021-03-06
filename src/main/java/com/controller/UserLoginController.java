package com.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dto.indto.UserEditDto;
import com.dto.indto.UserRegisterDto;
import com.dto.indto.UserUpdatePasswordDto;
import com.dto.indto.UserVerifyDto;
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
    private FinancialAdvisor fa;

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
        logger.info("??????????????????????????????" + JSONObject.toJSONString(user));
        UserRespDto userRespDto = new UserRespDto();
        try{
            //?????????????????????????????????????????????????????????????????????????????????????????????
            User userResp = mongoTemplate.findOne(query(where("openId").is(user.getOpenId()).and("isDelete").is(0)), User.class);
            if(!StringUtils.isEmpty(userResp)){
                BeanUtils.copyProperties(userResp, userRespDto);
                if (RoleCode.INVESTOR.getCode().equals(userResp.getRoleCode())) {
                    // ?????????????????????????????????
                    Investor investor = mongoTemplate.findOne(query(where("investorId").is(userResp.getUserId())), Investor.class);
                    userRespDto.setExtendData(investor);
                } else if (RoleCode.ENTUSER.getCode().equals(userResp.getRoleCode())) {
                    // ????????????????????????????????????
                    EntUser entUser = mongoTemplate.findOne(query(where("entUserId").is(userResp.getUserId())), EntUser.class);
                    userRespDto.setExtendData(entUser);
                } else if (RoleCode.FINANCIALADVISOR.getCode().equals(userResp.getRoleCode())) {
                    // ??????FA??????????????????
                    FinancialAdvisor financialAdvisor = mongoTemplate.findOne(query(where("faId").is(userResp.getUserId())), FinancialAdvisor.class);
                    userRespDto.setExtendData(financialAdvisor);
                }
            } else {
                String userId = commonUtils.getNumCode();
                user.setUserId(userId);
                user.setIsVerify(false);
                user.setCreateTime(new Date());
                user.setIsDelete(0);// ???????????????0-??????1-???
                user.setRoleCode(RoleCode.VISITOR.getCode());// ???????????????visitor-??????
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
     * ??????????????????
     * @param userId
     * @return
     */
    @GetMapping("/user/userDetail")
    public String userDetail(@RequestParam String userId) {
        UserRespDto userRespDto = new UserRespDto();
        //?????????????????????????????????????????????????????????????????????????????????????????????
        User userResp = mongoTemplate.findOne(query(where("userId").is(userId).and("isDelete").is(0)), User.class);
        if (!StringUtils.isEmpty(userResp)) {
            BeanUtils.copyProperties(userResp, userRespDto);
            if (RoleCode.INVESTOR.getCode().equals(userResp.getRoleCode())) {
                // ?????????????????????????????????
                Investor investor = mongoTemplate.findOne(query(where("investorId").is(userResp.getUserId())), Investor.class);
                userRespDto.setExtendData(investor);
            } else if (RoleCode.ENTUSER.getCode().equals(userResp.getRoleCode())) {
                // ????????????????????????????????????
                EntUser entUser = mongoTemplate.findOne(query(where("entUserId").is(userResp.getUserId())), EntUser.class);
                userRespDto.setExtendData(entUser);
            } else if (RoleCode.FINANCIALADVISOR.getCode().equals(userResp.getRoleCode())) {
                // ??????FA??????????????????
                FinancialAdvisor financialAdvisor = mongoTemplate.findOne(query(where("faId").is(userResp.getUserId())), FinancialAdvisor.class);
                userRespDto.setExtendData(financialAdvisor);
            }
        }
        OutputFormate outputFormate = new OutputFormate(userRespDto, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     * ??????????????????????????????????????????
     * @param entUser
     * @return
     */
    @PostMapping(value = "/entuser/entUserLogin")
    public String entUserLogin(@RequestBody EntUser entUser){
        try{
            //?????????????????????????????????????????????????????????????????????????????????????????????
            EntUser entUserResp = mongoTemplate.findOne(query(where("openId").is(entUser.getOpenId())),EntUser.class);
            if(!StringUtils.isEmpty(entUserResp)){
                // ??????????????????????????????????????????id?????????????????????????????????????????????????????????
                if (StringUtils.isEmpty(entUserResp.getEntUserId())) {
                    Update update = new Update();
                    String entUserId = commonUtils.getNumCode();
                    update.set("entUserId", entUserId);
                    mongoTemplate.updateFirst(query(where("openId").is(entUserResp.getOpenId())), update, EntUser.class);
                    entUserResp.setEntUserId(entUserId);

                    // ??????openId???????????????Project???entUserId???????????????????????????PC????????????entUserId??????????????????
                    Update projectUpdate = new Update();
                    projectUpdate.set("entUserId", entUserId);
                    mongoTemplate.updateMulti(query(where("openId").is(entUser.getOpenId())), update, Project.class);

                    // ??????openId???????????????ProjectComment???entUserId???????????????????????????PC????????????entUserId??????????????????
                    Update commentUpdate = new Update();
                    commentUpdate.set("entUserId", entUserId);
                    mongoTemplate.updateMulti(query(where("openId").is(entUser.getOpenId())), update, ProjectComment.class);
                }
                OutputFormate outputFormate = new OutputFormate(entUserResp, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
                return JSONObject.toJSONString(outputFormate);
            }else{
                String entUserId = commonUtils.getNumCode();
                // ??????????????????????????????????????????
                User user = mongoTemplate.findOne(query(where("phoneNm").is(entUser.getPhoneNm()).and("status").is(0)), User.class);
                if (null != user) {
                    entUserId = user.getUserId();
                }
                entUser.setEntUserId(entUserId);// ??????id??????????????????userId
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
     * ????????????
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
     * ??????????????????????????????
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
     * ??????????????????
     * @param userEditDto
     * @return
     */
    @PostMapping("/user/editUser")
    public String edit(MultipartFile photoFile, MultipartFile cardFile, MultipartFile cardBackFile, UserEditDto userEditDto) {
        logger.info("???????????????????????????" + JSONObject.toJSONString(userEditDto));
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
                // AWS S3????????????
                commonUtils.uploadFile(s3BucketName, photoFilePath, photoFile.getBytes());
                //????????????????????????????????????
                update.set("photoRoute", photoFilePath);
            }
            if (null != cardFile) {
                String filePath = entUserSavedFilepath + userEditDto.getUserId() + "/" + cardFile.getOriginalFilename();
                // AWS S3????????????
                commonUtils.uploadFile(s3BucketName, filePath, cardFile.getBytes());
                //????????????????????????????????????
                update.set("cardRoute", filePath);
            }
            if (null != cardBackFile) {
                String filePath = entUserSavedFilepath + userEditDto.getUserId() + "/" + cardBackFile.getOriginalFilename();
                // AWS S3????????????
                commonUtils.uploadFile(s3BucketName, filePath, cardBackFile.getBytes());
                //????????????????????????????????????
                update.set("cardBackRoute", filePath);
            }
            update.set("userName", userEditDto.getUserName());
            update.set("companyName", userEditDto.getCompanyName());
            update.set("positionName", userEditDto.getPositionName());
            update.set("email", userEditDto.getEmail());
            update.set("telephoneNo", userEditDto.getTelephoneNo());
            mongoTemplate.updateFirst(query(where("userId").is(userEditDto.getUserId())), update, User.class);

            // ???????????????????????????????????????????????????????????????
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
                faUpdate.set("orgNm", userEditDto.getCompanyName());
                faUpdate.set("positionName", userEditDto.getPositionName());
                mongoTemplate.updateFirst(query(where("faId").is(userEditDto.getUserId())), faUpdate, FinancialAdvisor.class);
            }

            User userResp = mongoTemplate.findOne(query(where("userId").is(userEditDto.getUserId())), User.class);
            OutputFormate outputFormate = new OutputFormate(userResp, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            logger.error("?????????????????????????????????", e);
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * ??????????????????(????????????)
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
     * ??????????????????
     * @param cardFile
     * @param user
     * @return
     */
    @PostMapping("/user/verify")
        public String verify(MultipartFile photoFile, MultipartFile cardFile, MultipartFile cardBackFile, UserVerifyDto user, String captcha, String targetRoleCode) {
        logger.info("????????????????????? user=" + JSONObject.toJSONString(user));
        logger.info("????????????????????? targetRoleCode=" + targetRoleCode);
        Map<String, Object> respMap = userLoginService.validateSms(user.getPhoneNm(), captcha);
        if (!(boolean)respMap.get("result")) {
            OutputFormate outputFormate = new OutputFormate("", ErrorCode.OTHEREEEOR.getCode(), String.valueOf(respMap.get("msg")));
            return JSONObject.toJSONString(outputFormate);
        }
        try {
            User userResp = mongoTemplate.findOne(query(where("userId").is(user.getUserId())), User.class);
            logger.info("??????????????????userId????????????????????? userResp=" + JSONObject.toJSONString(userResp));
            if (null != userResp && userResp.getIsVerify()) {
                OutputFormate outputFormate = new OutputFormate("", ErrorCode.OTHEREEEOR.getCode(), "??????????????????");
                return JSONObject.toJSONString(outputFormate);
            }
            Update update = new Update();
            if (null != photoFile) {
                String filePath = entUserSavedFilepath + user.getUserId() + "/" + photoFile.getOriginalFilename();
                // AWS S3????????????
                commonUtils.uploadFile(s3BucketName, filePath, photoFile.getBytes());
                //????????????????????????????????????
                update.set("photoRoute", filePath);
                user.setPhotoRoute(filePath);
            }
            if (null != cardFile) {
                String filePath = entUserSavedFilepath + user.getUserId() + "/" + cardFile.getOriginalFilename();
                // AWS S3????????????
                commonUtils.uploadFile(s3BucketName, filePath, cardFile.getBytes());
                //????????????????????????????????????
                update.set("cardRoute", filePath);
            }
            if (null != cardBackFile) {
                String filePath = entUserSavedFilepath + user.getUserId() + "/" + cardBackFile.getOriginalFilename();
                // AWS S3????????????
                commonUtils.uploadFile(s3BucketName, filePath, cardBackFile.getBytes());
                //????????????????????????????????????
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
            // ????????????
            mongoTemplate.updateFirst(query(where("userId").is(user.getUserId())), update, User.class);

            User userDto = mongoTemplate.findOne(query(where("userId").is(user.getUserId())), User.class);
            if (!StringUtils.isEmpty(userDto.getAuditStatus()) && 1 == userDto.getAuditStatus()) {
                // ???????????????????????????????????????????????????????????????????????????
                userService.sendAuditMail(userDto);
            }
            OutputFormate outputFormate = new OutputFormate(userDto, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
            logger.info("??????????????????...");
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            logger.error("???????????????????????????" + e);
            OutputFormate outputFormate = new OutputFormate("", ErrorCode.OTHEREEEOR.getCode(), e.getMessage());
            return JSONObject.toJSONString(outputFormate);
        }
    }

    /**
     * ???????????????????????????
     * @param user
     * @param targetRoleCode
     * @param update
     * @throws Exception
     */
    private void investorVerify(UserVerifyDto user, String targetRoleCode, Update update) throws Exception {
        // ?????????????????????????????????
        Investor investor = mongoTemplate.findOne(query(where("phoneNm").is(user.getPhoneNm())), Investor.class);
        if (null == investor && StringUtils.isEmpty(user.getEmail())) {
            throw new Exception("??????????????????????????????????????????????????????");
        }

        // ???????????????????????????????????????????????????
        if (!StringUtils.isEmpty(user.getEmail())) {
            update.set("userName", user.getUserName());
            update.set("companyName", user.getCompanyName());
            update.set("positionName", user.getPositionName());
            update.set("email", user.getEmail());
            update.set("telephoneNo", user.getTelephoneNo());
            update.set("roleCode", targetRoleCode);
            update.set("auditRoleCode", targetRoleCode);
            update.set("isVerify", false);
            update.set("auditStatus", 1);// ???????????????1-????????????2-???????????????3-???????????????
        }

        if (null == investor) {
            // ???????????????????????????????????????????????????????????????????????????
            Investor sysInvestor = mongoTemplate.findOne(query(where("investor").is(user.getUserName()).and("orgNm").is(user.getCompanyName() + "  |  " + user.getPhoneNm())), Investor.class);
            if (null != sysInvestor) {
                // ???????????????????????????????????????
                mongoTemplate.remove(query(where("investorId").is(sysInvestor.getInvestorId())), Investor.class);
            }
            // ?????????????????????????????????????????????
            Investor insertInvestor = new Investor();
            insertInvestor.setInvestorId(user.getUserId());
            insertInvestor.setInvestor(user.getUserName());
            insertInvestor.setInvesEmail(user.getEmail());
            insertInvestor.setStatus(1);// ?????????0-?????????1-??????
            insertInvestor.setShowFlag(user.getShowFlag());// ????????????????????????0-????????????1-?????????????????????
            insertInvestor.setOrgNm(user.getCompanyName() + "  |  " + user.getPositionName());
            insertInvestor.setPhoneNm(user.getPhoneNm());
            if (StringUtils.isEmpty(user.getPhotoRoute())) {
                // ?????????????????????????????????
                String photoRoute = "/home/ec2-user/data/investor/";
                int random = new Random().nextInt(13) + 1;
                String fileName = random + ".jpg";
                photoRoute = photoRoute + fileName;
                insertInvestor.setInvesPhotoRoute(photoRoute);
                update.set("photoRoute", photoRoute);// ?????????????????????
            } else {
                insertInvestor.setInvesPhotoRoute(user.getPhotoRoute());
                update.set("photoRoute", user.getPhotoRoute());// ?????????????????????
            }

            // ???????????????????????????????????????????????????
            insertInvestor.setFocusFiled(user.getFocusFiled());
            insertInvestor.setFinRound(user.getFinRound());
            insertInvestor.setFocusCity(user.getFocusCity());
            insertInvestor.setSelfIntroduction(user.getSelfIntroduction());

            mongoTemplate.insert(insertInvestor, "investor");
        } else {
            update.set("auditStatus", 2);// ???????????????1-????????????2-???????????????3-???????????????
            update.set("isVerify", true);
            Update investorUpdate = new Update();
            investorUpdate.set("investorId", user.getUserId());
            if (!StringUtils.isEmpty(user.getEmail())) {
                investorUpdate.set("investor", user.getUserName());
                investorUpdate.set("invesEmail", user.getEmail());
                investorUpdate.set("status", 0);// ?????????0-?????????1-??????
                investorUpdate.set("orgNm", user.getCompanyName() + "  |  " + user.getPositionName());
                investorUpdate.set("phoneNm", user.getPhoneNm());

                if (StringUtils.isEmpty(user.getPhotoRoute())) {
                    // ?????????????????????????????????
                    String photoRoute = "/home/ec2-user/data/investor/";
                    int random = new Random().nextInt(13) + 1;
                    String fileName = random + ".jpg";
                    photoRoute = photoRoute + fileName;
                    investorUpdate.set("photoRoute", photoRoute);
                    update.set("photoRoute", photoRoute);// ?????????????????????
                } else {
                    investorUpdate.set("photoRoute", user.getPhotoRoute());
                    update.set("photoRoute", user.getPhotoRoute());// ?????????????????????
                }

                // ???????????????????????????????????????????????????
                investorUpdate.set("focusFiled", user.getFocusFiled());
                investorUpdate.set("finRound", user.getFinRound());
                investorUpdate.set("focusCity", user.getFocusCity());
                investorUpdate.set("selfIntroduction", user.getSelfIntroduction());
            } else {
                update.set("userName", investor.getInvestor());
                if (!StringUtils.isEmpty(investor.getOrgNm()) && investor.getOrgNm().split("\\|").length == 2) {
                    update.set("companyName", investor.getOrgNm().split("\\|")[0].trim());
                    update.set("positionName", investor.getOrgNm().split("\\|")[1].trim());
                }
                update.set("email", investor.getInvesEmail());
                update.set("telephoneNo", investor.getPhoneNm());
                update.set("invesPhotoRoute", investor.getInvesPhotoRoute());
                update.set("cardRoute", investor.getInvesCardRoute());

            }
            investorUpdate.set("showFlag", 1);
            mongoTemplate.updateFirst(query(where("investorId").is(investor.getInvestorId())), investorUpdate, Investor.class);
        }
    }

    /**
     * ??????FA????????????
     * @param user
     * @param targetRoleCode
     * @param update
     * @throws Exception
     */
    private void financialAdvisorVerify(UserVerifyDto user, String targetRoleCode, Update update) throws Exception {
        // ?????????fa???????????????
        FinancialAdvisor financialAdvisor = mongoTemplate.findOne(query(where("phoneNm").is(user.getPhoneNm())), FinancialAdvisor.class);
        if (null == financialAdvisor && StringUtils.isEmpty(user.getEmail())) {
            throw new Exception("??????????????????????????????????????????????????????");
        }

        // ???????????????????????????????????????????????????
        if (!StringUtils.isEmpty(user.getEmail())) {
            update.set("userName", user.getUserName());
            update.set("companyName", user.getCompanyName());
            update.set("positionName", user.getPositionName());
            update.set("email", user.getEmail());
            update.set("telephoneNo", user.getTelephoneNo());
            update.set("roleCode", targetRoleCode);
            update.set("auditRoleCode", targetRoleCode);
            update.set("isVerify", false);
            update.set("auditStatus", 1);// ???????????????1-????????????2-???????????????3-???????????????
        }

        if (null == financialAdvisor) {
            // ???????????????????????????????????????????????????????????????????????????
            FinancialAdvisor sysFA = mongoTemplate.findOne(query(where("faName").is(user.getUserName()).and("orgNm").is(user.getCompanyName()).and("faType").is(1)), FinancialAdvisor.class);
            if (null != sysFA) {
                // ??????????????????????????????FA??????
                mongoTemplate.remove(query(where("faId").is(sysFA.getFaId())), FinancialAdvisor.class);
            }
            // ?????????????????????????????????????????????
            FinancialAdvisor fa = new FinancialAdvisor();
            fa.setFaType(1);// fa?????????1-fa??????,2-fa??????
            fa.setFaId(user.getUserId());
            fa.setFaName(user.getUserName());
            fa.setEmail(user.getEmail());
            fa.setStatus(1);// ?????????0-?????????1-??????
            fa.setShowFlag(user.getShowFlag());// ???????????????0-????????????1-FA????????????
            fa.setOrgNm(user.getCompanyName());
            fa.setPositionName(user.getPositionName());
            fa.setPhoneNm(user.getPhoneNm());
            if (StringUtils.isEmpty(user.getPhotoRoute())) {
                // ?????????????????????????????????
                String photoRoute = "/home/ec2-user/data/investor/";
                int random = new Random().nextInt(13) + 1;
                String fileName = random + ".jpg";
                photoRoute = photoRoute + fileName;
                fa.setPhotoRoute(photoRoute);
                update.set("photoRoute", photoRoute);// ?????????????????????
            } else {
                fa.setPhotoRoute(user.getPhotoRoute());
                update.set("photoRoute", user.getPhotoRoute());// ?????????????????????
            }

            // ???????????????????????????????????????????????????
            fa.setFocusFiled(user.getFocusFiled());
            fa.setFinRound(user.getFinRound());
            fa.setCity(user.getFocusCity());
            fa.setInvestmentCase(user.getInvestmentCase());
            fa.setSelfIntroduction(user.getSelfIntroduction());

            mongoTemplate.insert(fa, "financialAdvisor");
        } else {
            update.set("auditStatus", 2);// ???????????????1-????????????2-???????????????3-???????????????
            update.set("isVerify", true);

            Update faUpdate = new Update();
            if (!StringUtils.isEmpty(user.getEmail())) {
                faUpdate.set("faId", user.getUserId());
                faUpdate.set("faName", user.getUserName());
                faUpdate.set("email", user.getEmail());
                faUpdate.set("status", 0);// ?????????0-?????????1-??????
                faUpdate.set("orgNm", user.getCompanyName());
                faUpdate.set("phoneNm", user.getPhoneNm());
                faUpdate.set("positionName", user.getPositionName());
                faUpdate.set("faType", 1);
                if (StringUtils.isEmpty(user.getPhotoRoute())) {
                    // ?????????????????????????????????
                    String photoRoute = "/home/ec2-user/data/investor/";
                    int random = new Random().nextInt(13) + 1;
                    String fileName = random + ".jpg";
                    photoRoute = photoRoute + fileName;
                    faUpdate.set("photoRoute", photoRoute);
                    update.set("photoRoute", photoRoute);// ?????????????????????
                } else {
                    faUpdate.set("photoRoute", user.getPhotoRoute());
                    update.set("photoRoute", user.getPhotoRoute());// ?????????????????????
                }

                // ???????????????????????????????????????????????????
                faUpdate.set("focusFiled", user.getFocusFiled());
                faUpdate.set("finRound", user.getFinRound());
                faUpdate.set("focusCity", user.getFocusCity());
                faUpdate.set("selfIntroduction", user.getSelfIntroduction());
            } else {
                update.set("userName", financialAdvisor.getFaName());
                update.set("companyName", financialAdvisor.getOrgNm());
                update.set("positionName", financialAdvisor.getPositionName());
                update.set("email", financialAdvisor.getEmail());
                update.set("telephoneNo", financialAdvisor.getPhoneNm());
                update.set("photoRoute", financialAdvisor.getPhotoRoute());
            }
            faUpdate.set("showFlag", 1);
            mongoTemplate.updateFirst(query(where("faId").is(financialAdvisor.getFaId())), faUpdate, Investor.class);
        }
    }


    /**
     * ??????????????????????????????????????????
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



//    ---------------------------------????????????PC?????????????????????----------------------------------------------------

    /**
     * ????????????
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
     * ??????????????????
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
     * ??????????????????
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
     * ??????????????????
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
     * ??????userId?????????????????????
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
     * ?????????????????????
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
     * ??????s3??????
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




//    ----------------------?????????????????????--------------------
    private static String getToken() throws Exception {
        // access_token??????https????????????: GET https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
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
        // post???????????????
        Map<String, Object> map = new HashMap<>();
        map.put("type", "news"); // news???????????????????????????????????????API??????
        map.put("offset", 0);
        map.put("count", 1);
        // ???map?????????json?????????
        String paramBody = JSON.toJSONString(map); // ????????????Alibaba???fastjson

        OutputStream out = connection.getOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
        bw.write(paramBody); // ??????????????????????????????
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
