package com.service.impl;

import com.dto.indto.OrderDto;
import com.dto.outdto.OrderOutDto;
import com.dto.outdto.WxPayDto;
import com.enums.OrderBizType;
import com.pojo.IntegralGoods;
import com.pojo.Order;
import com.pojo.ProjectComment;
import com.service.OrderService;
import com.service.WxPayService;
import com.utils.CommonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

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
        String orderNo = commonUtils.getNumCode().substring(0, 32);// 订单号
//        // 微信统一支付
        WxPayDto wxPayDto = null;
        if (1 == orderDto.getPaymentType()) {
            wxPayDto = wxPayService.wxPay(orderNo, orderDto.getOpenId(), orderDto.getPayAmount().multiply(new BigDecimal("100")));
            wxPayDto.setOutTradeNo(orderNo);
        }
        // 初始化订单信息
        Order order = new Order();
        BeanUtils.copyProperties(orderDto, order);
        order.setOrderNo(orderNo);
        order.setPayStatus(1);// 支付状态：1-支付中，2-支付成功，3-支付失败，4-支付异常,5-支付取消
        order.setCreateTime(new Date());
        if (1 == orderDto.getPaymentType() && null == wxPayDto) {
            order.setPayStatus(3);
        }
        mongoTemplate.insert(order, "order");
        return wxPayDto;
    }

    @Override
    public Order detailByOrderNo(String orderNo) {
        return mongoTemplate.findOne(query(where("orderNo").is(orderNo)), Order.class);
    }

    @Override
    public void update(Order order) {
        Update update = new Update();
        if (!StringUtils.isEmpty(order.getTransactionId())) {
            update.set("transactionId", order.getTransactionId());
        }
        if (null != order.getPayStatus()) {
            update.set("payStatus", order.getPayStatus());
        }
        if (null != order.getBizStatus()) {
            update.set("bizStatus", order.getBizStatus());
        }
        if (null != order.getPhoneNm()) {
            update.set("phoneNm", order.getPhoneNm());
        }
        if (null != order.getUserName()) {
            update.set("userName", order.getUserName());
        }
        mongoTemplate.updateFirst(query(where("orderNo").is(order.getOrderNo())), update, Order.class);
    }

    @Override
    public void delete(String orderNo) {
        mongoTemplate.remove(query(where("orderNo").is(orderNo)), Order.class);
    }

    @Override
    public void status(String orderNo, Integer payStatus) {
        Update update = new Update();
        update.set("payStatus", payStatus);
        update.set("updateTime", new Date());
        mongoTemplate.updateFirst(query(where("orderNo").is(orderNo)), update, Order.class);
    }

    @Override
    public void statusByBizId(String bizId, Integer payStatus) {
        Update update = new Update();
        update.set("payStatus", payStatus);
        update.set("updateTime", new Date());
        mongoTemplate.updateFirst(query(where("bizId").is(bizId)), update, Order.class);
    }

    @Override
    public Integer count(String userId) {
        Criteria criteria = new Criteria();
        if (null != userId) {
            List<Integer> bizTypeList = new ArrayList<>();
            bizTypeList.add(OrderBizType.PURCHASEMONTHVIP.getCode());
            bizTypeList.add(OrderBizType.PURCHASEQUARTERVIP.getCode());
            bizTypeList.add(OrderBizType.PURCHASEYEARVIP.getCode());
            List<Integer> payStatusList = new ArrayList<>();
            payStatusList.add(1);
            payStatusList.add(5);
            criteria = where("userId").is(userId).and("bizType").nin(bizTypeList).and("payStatus").nin(payStatusList);
        }
        return (int) mongoTemplate.count(new Query(criteria),"order");
    }

    @Override
    public List<OrderOutDto> pageList(Integer pageNum, Integer pageSize, String openId, String userId) {
        Criteria criteria = new Criteria();
        if (null != userId) {
            List<Integer> bizTypeList = new ArrayList<>();
            bizTypeList.add(OrderBizType.PURCHASEMONTHVIP.getCode());
            bizTypeList.add(OrderBizType.PURCHASEQUARTERVIP.getCode());
            bizTypeList.add(OrderBizType.PURCHASEYEARVIP.getCode());
            List<Integer> payStatusList = new ArrayList<>();
            payStatusList.add(1);
            payStatusList.add(5);
            criteria = where("userId").is(userId).and("bizType").nin(bizTypeList).and("payStatus").nin(payStatusList);
        }
        int startNum = pageNum * pageSize;
        List<Order> orders = mongoTemplate.find(new Query(criteria).skip(startNum).limit(pageSize), Order.class);
        List<OrderOutDto> orderOutDtos = new ArrayList<>();
        OrderOutDto orderOutDto = null;
        if (!CollectionUtils.isEmpty(orders)) {
            for (Order order : orders) {
                orderOutDto = new OrderOutDto();
                BeanUtils.copyProperties(order, orderOutDto);
                orderOutDtos.add(orderOutDto);
            }
        }
        return orderOutDtos;
    }

    @Override
    public void commitReply(Order order) {
        Update update = new Update();
        update.set("stars", order.getStars());
        update.set("reply", order.getReply());
        update.set("replyTm", new Date());
        update.set("bizStatus", 4);
        mongoTemplate.updateFirst(query(where("orderNo").is(order.getOrderNo())), update, Order.class);
    }

    @Override
    public Integer countAll(String userId) {
        Criteria criteria = new Criteria();
        if (null != userId) {
            criteria = where("userId").is(userId);
        }
        return (int) mongoTemplate.count(new Query(criteria),"order");
    }

    @Override
    public List<OrderOutDto> pageListAll(Integer pageNum, Integer pageSize, String userId) {
        Criteria criteria = new Criteria();
        if (null != userId) {
            criteria = where("userId").is(userId);
        }
        int startNum = pageNum * pageSize;
        List<Order> orders = mongoTemplate.find(new Query(criteria).skip(startNum).limit(pageSize), Order.class);
        List<OrderOutDto> orderOutDtos = new ArrayList<>();
        OrderOutDto orderOutDto = null;
        if (!CollectionUtils.isEmpty(orders)) {
            for (Order order : orders) {
                orderOutDto = new OrderOutDto();
                BeanUtils.copyProperties(order, orderOutDto);
                orderOutDtos.add(orderOutDto);
            }
        }
        return orderOutDtos;
    }

}
