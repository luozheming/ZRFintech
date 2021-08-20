package com.service.manage;

import com.pojo.ChargeItem;

import java.util.List;

public interface ChargeItemService {
    ChargeItem detail(String id);
    List<ChargeItem> list();
    void update(ChargeItem ChargeItem);
    void insert(ChargeItem ChargeItem);
}
