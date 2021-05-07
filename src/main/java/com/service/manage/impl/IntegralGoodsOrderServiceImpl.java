package com.service.manage.impl;

import com.pojo.IntegralGoodsOrder;
import com.pojo.UsualAddress;
import com.service.manage.IntegralGoodsOrderService;
import com.utils.CommonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class IntegralGoodsOrderServiceImpl implements IntegralGoodsOrderService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CommonUtils commonUtils;

    @Override
    public void add(IntegralGoodsOrder integralGoodsOrder) {
        // 地址为空时新增收货地址
        if (StringUtils.isEmpty(integralGoodsOrder.getUsualAddressId())) {
            UsualAddress usualAddress = new UsualAddress();
            BeanUtils.copyProperties(integralGoodsOrder, usualAddress);
            String usualAddressId = commonUtils.getNumCode();
            usualAddress.setId(usualAddressId);
            mongoTemplate.save(usualAddress);
            integralGoodsOrder.setUsualAddressId(usualAddressId);
        }

        // 记录商品及收货人关联信息
        integralGoodsOrder.setId(commonUtils.getNumCode());
        mongoTemplate.save(integralGoodsOrder);
    }
}
