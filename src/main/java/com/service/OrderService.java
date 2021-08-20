package com.service;

import com.dto.indto.OrderDto;
import com.dto.outdto.OrderOutDto;
import com.dto.outdto.WxPayDto;
import com.pojo.Order;

import java.util.List;

public interface OrderService {
    WxPayDto createOrder(OrderDto orderDto) throws Exception;
    Order detailByOrderNo(String orderNo);
    void update(Order order);
    void delete(String orderNo);
    void status(String orderNo, Integer payStatus);
    void statusByBizId(String bizId, Integer payStatus);
    Integer count(String openId);
    List<OrderOutDto> pageList(Integer pageNum, Integer pageSize, String openId, String userId);
    void commitReply(Order order);
    Integer countAll(String openId);
    List<OrderOutDto> pageListAll(Integer pageNum, Integer pageSize, String userId);
}
