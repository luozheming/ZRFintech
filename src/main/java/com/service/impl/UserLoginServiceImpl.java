package com.service.impl;

import com.dto.indto.EntUserLoginDto;
import com.dto.indto.EntUserRegisterDto;
import com.dto.indto.EntUserUpdatePasswordDto;
import com.pojo.EntUser;
import com.service.UserLoginService;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

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
        // 通过手机号码查找用户是否存在
        EntUser entUser = mongoTemplate.findOne(query(where("phoneNm").is(entUserRegisterDto.getPhoneNm())), EntUser.class);
        if (null != entUser && !StringUtils.isEmpty(entUser.getPassword())) {
            throw new Exception(ErrorCode.EXISTSUSER.getMessage());
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
            mongoTemplate.save(entUserAdd);
        } else {
            // 如果小程序之前登录过，则更新这个用户的密码相关信息
            Update update = new Update();
            update.set("userId", userId);
            update.set("password", password);
            update.set("updateTime", new Date());
            update.set("roleCode", entUserRegisterDto.getRoleCode());
            mongoTemplate.updateFirst(query(where("phoneNm").is(entUser.getPhoneNm())), update, EntUser.class);
        }
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
        return entUser;
    }

    @Override
    public void updatePassword(EntUserUpdatePasswordDto entUserUpdatePasswordDto) throws Exception {
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
}
