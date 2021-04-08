package com.service.impl;

import com.dto.indto.OrderDto;
import com.dto.outdto.WxPayDto;
import com.pojo.Order;
import com.service.OrderService;
import com.service.WxPayService;
import com.utils.CommonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public WxPayDto createOrder(OrderDto orderDto) throws Exception {
        // 生成订单号
        String orderNo = commonUtils.getNumCode();// 订单号
        // 微信统一支付
        WxPayDto wxPayDto = wxPayService.wxPay(orderDto.getOpenId(), orderDto.getPayAmount());
        // 初始化订单信息
        Order order = new Order();
        BeanUtils.copyProperties(orderDto, order);
        order.setOrderNo(orderNo);
        order.setPayStatus(0);// 支付状态：0-未支付，1-支付中，2-支付成功，3-支付失败，4-支付超时
        if (null == wxPayDto) {
            order.setPayStatus(3);
        } else {
            order.setPayStatus(1);
        }
        mongoTemplate.insert(order, "order");
        return wxPayDto;
    }
}
