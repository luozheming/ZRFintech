package com.service.manage;

import com.dto.outdto.HomePageDto;
import com.pojo.User;

public interface UserService {
    User login(String phoneNm, String password);

    User getById(String userId);

    HomePageDto homePage();
}
