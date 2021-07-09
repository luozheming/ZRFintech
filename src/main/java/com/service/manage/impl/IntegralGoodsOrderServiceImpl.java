package com.service.manage.impl;

import com.pojo.EntUser;
import com.pojo.IntegralGoodsOrder;
import com.pojo.Investor;
import com.pojo.UsualAddress;
import com.service.manage.IntegralGoodsOrderService;
import com.utils.CommonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

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
        integralGoodsOrder.setDealStatus(0);
        integralGoodsOrder.setCreateTime(new Date());
        mongoTemplate.save(integralGoodsOrder);

        // 查询投资人剩余积分,扣除投资人相应积分
        Investor investor = mongoTemplate.findOne(query(where("investorId").is(integralGoodsOrder.getUserId())), Investor.class);
        BigDecimal surplusAmount = investor.getSurplusAmount().subtract(integralGoodsOrder.getIntegral());
        // 更新投资人剩余积分
        Update update = new Update();
        update.set("surplusAmount", surplusAmount);
        mongoTemplate.updateFirst(query(where("investorId").is(investor.getInvestorId())), update, Investor.class);
    }

    @Override
    public List<IntegralGoodsOrder> pageList(Integer pageNum, Integer pageSize) {
        int startNum = pageNum * pageSize;
        List<IntegralGoodsOrder> integralGoodsOrders = mongoTemplate.find(new Query().with(Sort.by(Sort.Order.asc("createTime"))).skip(startNum).limit(pageSize), IntegralGoodsOrder.class);
        return integralGoodsOrders;
    }

    @Override
    public Integer count() {
        return (int)mongoTemplate.count(new Query(), IntegralGoodsOrder.class);
    }

    @Override
    public void dealStatus(String id, Integer dealStatus) {
        Update update = new Update();
        update.set("dealStatus", dealStatus);
        update.set("updateTime", new Date());
        mongoTemplate.updateFirst(query(where("id").is(id)), update, IntegralGoodsOrder.class);
    }

    @Override
    public Investor investorById(String userId) {
        Investor investor = mongoTemplate.findOne(query(where("investorId").is(userId)), Investor.class);
        return investor;
    }
}
