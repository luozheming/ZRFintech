package com.service.impl;

import com.dto.indto.UserRegisterDto;
import com.dto.indto.UserUpdatePasswordDto;
import com.dto.outdto.UserLoginDto;
import com.pojo.EntUser;
import com.pojo.Investor;
import com.pojo.SmsCaptcha;
import com.pojo.User;
import com.service.UserLoginService;
import com.utils.CommonUtils;
import com.utils.DateUtil;
import com.utils.ErrorCode;
import com.utils.SendSmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
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
    public void register(UserRegisterDto userRegisterDto) throws Exception {
        Map<String, Object> respMap = validateSms(userRegisterDto.getPhoneNm(), userRegisterDto.getCaptcha());
        if (!(boolean)respMap.get("result")) {
            throw new Exception(String.valueOf(respMap.get("msg")));
        }

        // 通过手机号码查找用户是否存在
        User user = mongoTemplate.findOne(query(where("phoneNm").is(userRegisterDto.getPhoneNm())), User.class);
        if (null != user && !StringUtils.isEmpty(user.getPassword())) {
            throw new Exception(ErrorCode.EXISTSUSER.getMessage());
        }

        String userId = commonUtils.getNumCode();// 新注册用户id
        Investor investor = null;
        if ("investor".equals(userRegisterDto.getRoleCode())) {
            investor = mongoTemplate.findOne(query(where("phoneNm").is(userRegisterDto.getPhoneNm())), Investor.class);
            if (null != investor) {
                userId = investor.getInvestorId();
            } else {
                throw new Exception("需联系管理员申请注册为投资人");
            }
        } else if ("ent".equals(userRegisterDto.getRoleCode())) {
            EntUser entUser = mongoTemplate.findOne(query(where("phoneNm").is(userRegisterDto.getPhoneNm())), EntUser.class);
            if (null != entUser) {
                if (!StringUtils.isEmpty(entUser.getEntUserId())) {
                    userId = entUser.getEntUserId();
                } else {
                    // 如果已入库的客户信息没有客户id则更新一个
                    Update update = new Update();
                    update.set("entUserId", userId);
                    mongoTemplate.updateFirst(query(where("openId").is(entUser.getOpenId())), update, EntUser.class);
                }
            }
        }

        String password = bCryptPasswordEncoder.encode(userRegisterDto.getPassword());
        if (null == user) {
            // 新增
            User userAdd = new User();
            userAdd.setUserId(userId);
            userAdd.setPhoneNm(userRegisterDto.getPhoneNm());
            userAdd.setPassword(password);
            userAdd.setCreateTime(new Date());
            userAdd.setRoleCode(userRegisterDto.getRoleCode());
            userAdd.setUserName("QR" + commonUtils.getIntCode(6));
            mongoTemplate.save(userAdd);
        }

        // 更新验证码失效
        updateSmsStatus(userRegisterDto.getPhoneNm());
    }

    @Override
    public UserLoginDto loginByPassword(User User) throws Exception {
        UserLoginDto userLoginDto = new UserLoginDto();
        // 通过手机号码查找用户是否存在
        User user = mongoTemplate.findOne(query(where("phoneNm").is(User.getPhoneNm())), User.class);
        if (null == user) {
            throw new Exception(ErrorCode.EMPITYUSER.getMessage());
        }

        // 判断密码是否正确
        boolean pass = bCryptPasswordEncoder.matches(User.getPassword(), user.getPassword());
        if (!pass) {
            throw new Exception(ErrorCode.ERRORPASSWORD.getMessage());
        }

        // 根据不同角色返回对应信息
        if ("ent".equals(user.getRoleCode())) {
            EntUser entUser = mongoTemplate.findOne(query(where("entUserId").is(user.getUserId())), EntUser.class);
            if (StringUtils.isEmpty(entUser.getPhotoRoute())) {
                userLoginDto.setPhotoRoute(commonUtils.getFullFilePath(entUser.getPhotoRoute()));
            }
        } else if ("investor".equals(user.getRoleCode())) {
            Investor investor = mongoTemplate.findOne(query(where("investorId").is(user.getUserId())), Investor.class);
            if (StringUtils.isEmpty(investor.getInvesPhotoRoute())) {
                userLoginDto.setPhotoRoute(commonUtils.getFullFilePath(investor.getInvesPhotoRoute()));
            }
        } else if ("admin".equals(user.getRoleCode())) {

        }

        return userLoginDto;
    }

    @Override
    public void updatePassword(UserUpdatePasswordDto userUpdatePasswordDto) throws Exception {
        Map<String, Object> respMap = validateSms(userUpdatePasswordDto.getPhoneNm(), userUpdatePasswordDto.getCaptcha());
        if (!(boolean)respMap.get("result")) {
            throw new Exception(String.valueOf(respMap.get("msg")));
        }

        // 通过手机号码查找用户是否存在
        User user = mongoTemplate.findOne(query(where("phoneNm").is(userUpdatePasswordDto.getPhoneNm()).and("status").is(0)), User.class);
        if (null == user) {
            throw new Exception(ErrorCode.EMPITYUSER.getMessage());
        }

        // 判断原密码是否正确
        boolean pass = bCryptPasswordEncoder.matches(userUpdatePasswordDto.getOrgPassword(), user.getPassword());
        if (!pass) {
            throw new Exception(ErrorCode.ERRORPASSWORD.getMessage());
        }

        // 更新密码
        String password = bCryptPasswordEncoder.encode(userUpdatePasswordDto.getPassword());
        Update update = new Update();
        update.set("password", password);
        update.set("updateTime", new Date());
        mongoTemplate.updateFirst(query(where("phoneNm").is(user.getPhoneNm())), update, User.class);

        // 更新验证码失效
        updateSmsStatus(userUpdatePasswordDto.getPhoneNm());
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
        mongoTemplate.updateFirst(query(where("entUserId").is(entUser.getEntUserId())), update, EntUser.class);
    }

    @Override
    public EntUser detail(String userId) {
        EntUser entUser = mongoTemplate.findOne(query(where("userId").is(userId)), EntUser.class);
        if (null != entUser & !StringUtils.isEmpty(entUser.getPhotoRoute())) {
            entUser.setPhotoRoute(commonUtils.getFullFilePath(entUser.getPhotoRoute()));
        }
        return entUser;
    }

    @Override
    public Investor investorById(String userId) {
        Investor investor = mongoTemplate.findOne(query(where("investorId").is(userId)), Investor.class);
        return investor;
    }

    @Override
    public void sendSms(Integer smsType, String phoneNm) throws Exception {
        String templateId = "";
        if (0 == smsType) {
            templateId ="97c4b97d58ff4414be0929f2db408b96";// 通用验证码
        } else if (1 == smsType) {
            templateId = "33180c3884554cb094a4985b31943388";// 修改密码短信模板
        } else if (2 == smsType) {
            templateId ="553ed74f719e41a1b15f5033aa5e4b43";// 用户注册短信模板
        } else if (3 == smsType) {
            templateId ="e2f2ad522e774b92885889a4d8dfa45a";// 客户认证
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
    public Map<String, Object> validateSms(String phoneNm, String captcha) {
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
