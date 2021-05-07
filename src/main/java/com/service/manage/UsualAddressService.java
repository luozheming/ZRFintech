package com.service.manage;

import com.pojo.UsualAddress;

import java.util.List;

public interface UsualAddressService {
    List<UsualAddress> listByUserId(String userId);
    void edit(UsualAddress usualAddress);
    void add(UsualAddress usualAddress);
}
