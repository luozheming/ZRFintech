package com.service;

import com.dto.indto.OrderDto;
import com.dto.outdto.WxPayDto;

public interface OrderService {
    WxPayDto createOrder(OrderDto orderDto) throws Exception;
}
