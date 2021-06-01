package com.service;

import com.dto.indto.OrderDto;
import com.dto.outdto.WxPayDto;
import com.pojo.Order;

public interface OrderService {
    WxPayDto createOrder(OrderDto orderDto) throws Exception;
    Order detailByOrderNo(String orderNo);
    void update(Order order);
    void delete(String orderNo);
    void status(String orderNo, Integer payStatus);
    void statusByBizId(String bizId, Integer payStatus);
}
