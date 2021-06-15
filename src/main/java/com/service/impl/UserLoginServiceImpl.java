package com.service.impl;

import com.dto.indto.EntUserLoginDto;
import com.dto.indto.EntUserRegisterDto;
import com.dto.indto.EntUserUpdatePasswordDto;
import com.pojo.EntUser;
import com.pojo.Investor;
import com.pojo.SmsCaptcha;
import com.service.UserLoginService;
import com.utils.CommonUtils;
import com.utils.DateUtil;
import com.utils.ErrorCode;
import com.utils.SendSmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void register(EntUserRegisterDto entUserRegisterDto) throws Exception {
        Map<String, Object> respMap = validateSms(entUserRegisterDto.getPhoneNm(), entUserRegisterDto.getCaptcha());
        if (!(boolean)respMap.get("result")) {
            throw new Exception(String.valueOf(respMap.get("msg")));
        }

        // 通过手机号码查找用户是否存在
        EntUser entUser = mongoTemplate.findOne(query(where("phoneNm").is(entUserRegisterDto.getPhoneNm())), EntUser.class);
        if (null != entUser && !StringUtils.isEmpty(entUser.getPassword())) {
            throw new Exception(ErrorCode.EXISTSUSER.getMessage());
        }
        Investor investor = null;
        if ("investor".equals(entUserRegisterDto.getRoleCode())) {
            investor = mongoTemplate.findOne(query(where("phoneNm").is(entUserRegisterDto.getPhoneNm())), Investor.class);
            if (null == investor) {
                throw new Exception("需联系管理员申请注册为投资人");
            }
        }
        String userId = commonUtils.getNumCode();
        String password = bCryptPasswordEncoder.encode(entUserRegisterDto.getPassword());
        if (null == entUser) {
            // 新增
            EntUser entUserAdd = new EntUser();
            entUserAdd.setUserId(userId);
            entUserAdd.setPhoneNm(entUserRegisterDto.getPhoneNm());
            entUserAdd.setPassword(password);
            entUserAdd.setCreateTime(new Date());
            entUserAdd.setRoleCode(entUserRegisterDto.getRoleCode());
            if ("investor".equals(entUserRegisterDto.getRoleCode())) {
                entUserAdd.setInvestorId(investor.getInvestorId());
            }
            entUserAdd.setUserName("QR" + commonUtils.getIntCode(6));
            mongoTemplate.save(entUserAdd);
        } else {
            // 如果小程序之前登录过，则更新这个用户的密码相关信息
            Update update = new Update();
            update.set("userId", userId);
            update.set("password", password);
            update.set("updateTime", new Date());
            update.set("roleCode", entUserRegisterDto.getRoleCode());
            if ("investor".equals(entUserRegisterDto.getRoleCode())) {
                update.set("investorId", investor.getInvestorId());
            }
            update.set("userName", "QR" + commonUtils.getIntCode(6));
            mongoTemplate.updateFirst(query(where("phoneNm").is(entUser.getPhoneNm())), update, EntUser.class);
        }

        // 更新验证码失效
        updateSmsStatus(entUserRegisterDto.getPhoneNm());
    }

    @Override
    public EntUser login(EntUserLoginDto entUserLoginDto) throws Exception {
        // 通过手机号码查找用户是否存在
        EntUser entUser = mongoTemplate.findOne(query(where("phoneNm").is(entUserLoginDto.getPhoneNm())), EntUser.class);
        if (null == entUser) {
            throw new Exception(ErrorCode.EMPITYUSER.getMessage());
        }

        // 判断密码是否正确
        boolean pass = bCryptPasswordEncoder.matches(entUserLoginDto.getPassword(), entUser.getPassword());
        if (!pass) {
            throw new Exception(ErrorCode.ERRORPASSWORD.getMessage());
        }
        String phoneNm = entUser.getPhoneNm();
        Investor investor = mongoTemplate.findOne(query(where("phoneNm").is(phoneNm)), Investor.class);
        if (null != investor) {
            entUser.setInvestorId(investor.getInvestorId());
            if (StringUtils.isEmpty(entUser.getPhotoRoute())) {

            }
        }
        if (!StringUtils.isEmpty(entUser.getPhotoRoute())) {
            entUser.setPhoto(commonUtils.getPhoto(entUser.getPhotoRoute()));
        }
        return entUser;
    }

    @Override
    public void updatePassword(EntUserUpdatePasswordDto entUserUpdatePasswordDto) throws Exception {
        Map<String, Object> respMap = validateSms(entUserUpdatePasswordDto.getPhoneNm(), entUserUpdatePasswordDto.getCaptcha());
        if (!(boolean)respMap.get("result")) {
            throw new Exception(String.valueOf(respMap.get("msg")));
        }

        // 通过手机号码查找用户是否存在
        EntUser entUser = mongoTemplate.findOne(query(where("phoneNm").is(entUserUpdatePasswordDto.getPhoneNm())), EntUser.class);
        if (null == entUser) {
            throw new Exception(ErrorCode.EMPITYUSER.getMessage());
        }

        // 判断原密码是否正确
        boolean pass = bCryptPasswordEncoder.matches(entUserUpdatePasswordDto.getOrgPassword(), entUser.getPassword());
        if (!pass) {
            throw new Exception(ErrorCode.ERRORPASSWORD.getMessage());
        }

        // 更新密码
        String password = bCryptPasswordEncoder.encode(entUserUpdatePasswordDto.getPassword());
        Update update = new Update();
        update.set("password", password);
        update.set("updateTime", new Date());
        mongoTemplate.updateFirst(query(where("phoneNm").is(entUser.getPhoneNm())), update, EntUser.class);

        // 更新验证码失效
        updateSmsStatus(entUserUpdatePasswordDto.getPhoneNm());
    }

    @Override
    public void edit(EntUser entUser) throws Exception {
        Update update = new Update();
        if (!StringUtils.isEmpty(entUser.getPhotoRoute())) {
            update.set("photoRoute", entUser.getPhotoRoute());
        }
        update.set("phoneNm", entUser.getPhoneNm());
        update.set("userName", entUser.getUserName());
        update.set("weChatNm", entUser.getWeChatNm());
        mongoTemplate.updateFirst(query(where("userId").is(entUser.getUserId())), update, EntUser.class);
    }

    @Override
    public EntUser detail(String userId) {
        EntUser entUser = mongoTemplate.findOne(query(where("userId").is(userId)), EntUser.class);
        if (null != entUser & !StringUtils.isEmpty(entUser.getPhotoRoute())) {
            entUser.setPhoto(commonUtils.getPhoto(entUser.getPhotoRoute()));
        }
        return entUser;
    }

    @Override
    public Investor investorById(String userId) {
        EntUser entUser = mongoTemplate.findOne(query(where("userId").is(userId)), EntUser.class);
        Investor investor = mongoTemplate.findOne(query(where("investorId").is(entUser.getInvestorId())), Investor.class);
        return investor;
    }

    @Override
    public void sendSms(Integer smsType, String phoneNm) throws Exception {
        String templateId = "";
        if (1 == smsType) {
            templateId = "33180c3884554cb094a4985b31943388";// 修改密码短信模板
        } else if (2 == smsType) {
            templateId ="553ed74f719e41a1b15f5033aa5e4b43";// 用户注册短信模板
        }
        String captcha = SendSmsUtil.sendSms(phoneNm, templateId);
        if (!StringUtils.isEmpty(captcha)) {
            // 更新该手机号码申请的验证码均失效
            Update update = new Update();
            update.set("status", 1);
            mongoTemplate.updateFirst(query(where("phoneNm").is(phoneNm).and("status").is(0)), update, SmsCaptcha.class);

            // 插入验证码表数据
            SmsCaptcha smsCaptcha = new SmsCaptcha();
            smsCaptcha.setId(commonUtils.getNumCode());
            smsCaptcha.setPhoneNm(phoneNm);
            smsCaptcha.setCaptcha(captcha);
            smsCaptcha.setStatus(0);
            smsCaptcha.setCreateTime(new Date());
            smsCaptcha.setExpires(DateUtil.getNextDate(smsCaptcha.getCreateTime(), 2, 5));
            mongoTemplate.save(smsCaptcha);
        }
    }

    /**
     * 短信验证码核对
     * @param phoneNm
     * @param captcha
     * @return
     */
    private Map<String, Object> validateSms(String phoneNm, String captcha) {
        Map<String, Object> respMap = new HashMap<>();
        respMap.put("result", true);
        // 短信验证码核对
        List<SmsCaptcha> smsCaptchas = mongoTemplate.find(query(where("phoneNm").is(phoneNm).and("status").is(0)).with(Sort.by(Sort.Order.desc("createTime"))), SmsCaptcha.class);
        if (CollectionUtils.isEmpty(smsCaptchas)) {
            respMap.put("result", false);
            respMap.put("msg", "验证码已过期");
        } else {
            SmsCaptcha smsCaptcha = smsCaptchas.get(0);
            if (!captcha.equals(smsCaptcha.getCaptcha())) {
                respMap.put("result", false);
                respMap.put("msg", "验证码不正确");
            } else if (new Date().compareTo(smsCaptcha.getExpires()) == 1) {
                respMap.put("result", false);
                respMap.put("msg", "验证码已过期");
            }
        }
        return respMap;
    }

    /**
     * 更新验证码状态
     * @param phoneNm
     */
    private void updateSmsStatus(String phoneNm) {
        // 更新验证码失效
        Update update = new Update();
        update.set("status", 1);
        mongoTemplate.updateFirst(query(where("phoneNm").is(phoneNm).and("status").is(0)), update, SmsCaptcha.class);
    }
}
