package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.indto.OrderDto;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.WxPayDto;
import com.service.OrderService;
import com.service.WxPayService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private WxPayService wxPayService;

    @PostMapping("/createOrder")
    public String createOrder(@RequestBody OrderDto orderDto ) {
        WxPayDto wxPayDto = null;
        try {
           wxPayDto = orderService.createOrder(orderDto);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        OutputFormate outputFormate = new OutputFormate(wxPayDto, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }
}
