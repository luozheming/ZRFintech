package com.controller.manage;

import com.pojo.IntegralGoodsOrder;
import com.service.manage.IntegralGoodsOrderService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController("/integralGoodsOrder")
public class IntegralGoodsOrderController {

    @Autowired
    private IntegralGoodsOrderService integralGoodsOrderService;

    public String add(IntegralGoodsOrder integralGoodsOrder) {
        integralGoodsOrderService.add(integralGoodsOrder);
        return ErrorCode.SUCCESS.toJsonString();
    }
}
