package com.service;

import com.dto.indto.UserRegisterDto;
import com.dto.indto.UserUpdatePasswordDto;
import com.dto.outdto.UserLoginDto;
import com.pojo.EntUser;
import com.pojo.Investor;
import com.pojo.User;

public interface UserLoginService {
    void register(UserRegisterDto userRegisterDto) throws Exception;
    UserLoginDto loginByPassword(User User) throws Exception;
    void updatePassword(UserUpdatePasswordDto userUpdatePasswordDto) throws Exception;
    void edit(EntUser entUser) throws Exception;
    EntUser detail(String userId);
    Investor investorById(String userId);
    void sendSms(Integer smsType, String phoneNm) throws Exception;
}
