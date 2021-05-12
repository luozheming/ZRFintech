package com.service.manage;

import com.pojo.IntegralGoodsOrder;
import com.pojo.Investor;

import java.util.List;

public interface IntegralGoodsOrderService {
    void add(IntegralGoodsOrder integralGoodsOrder);
    List<IntegralGoodsOrder> pageList(Integer pageNum, Integer pageSize);
    Integer count();
    void dealStatus(String id, Integer dealStatus);
    Investor investorById(String userId);
}
