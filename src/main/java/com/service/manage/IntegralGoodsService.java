package com.service.manage;

import com.pojo.IntegralGoods;

import java.math.BigDecimal;
import java.util.List;

public interface IntegralGoodsService {
    List<IntegralGoods> pageList(Integer pageNum, Integer pageSize, BigDecimal integralStart, BigDecimal integralEnd);
    Integer count();
    IntegralGoods detail(String id);
    void add(IntegralGoods integralGoods);
    void edit(IntegralGoods integralGoods);
}
