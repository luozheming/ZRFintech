package com.service;

import com.dto.indto.EntUserLoginDto;
import com.dto.indto.EntUserRegisterDto;
import com.dto.indto.EntUserUpdatePasswordDto;
import com.pojo.EntUser;

public interface UserLoginService {
    void register(EntUserRegisterDto entUserRegisterDto) throws Exception;
    void login(EntUserLoginDto entUserLoginDto) throws Exception;
    void updatePassword(EntUserUpdatePasswordDto entUserUpdatePasswordDto) throws Exception;
    void edit(EntUser entUser) throws Exception;
    EntUser detail(String phoneNm);
}
