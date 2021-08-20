package com.service.manage.impl;

import com.pojo.ChargeItem;
import com.pojo.VIPCard;
import com.service.manage.ChargeItemService;
import com.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class ChargeItemServiceImpl implements ChargeItemService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CommonUtils commonUtils;

    @Override
    public ChargeItem detail(String id) {
        return mongoTemplate.findOne(query(where("id").is(id)), ChargeItem.class);
    }

    @Override
    public List<ChargeItem> list() {
        Query query = new Query();
        query.with(Sort.by(Sort.Order.asc("chargeItemType")));
        return mongoTemplate.find(query, ChargeItem.class);
    }

    @Override
    public void update(ChargeItem chargeItem) {
        Update update = new Update();
        if (null != chargeItem.getPrice()) {
            update.set("price", chargeItem.getPrice());
        }
        if (null != chargeItem.getDiscountPrice()) {
            update.set("discountPrice", chargeItem.getDiscountPrice());
        }
        Integer chargeItemType = chargeItem.getChargeItemType();
        if (8 == chargeItemType || 9 == chargeItemType || 10 == chargeItemType) {
            if (8 == chargeItemType) {
                chargeItemType = 1;
            } else if (9 == chargeItemType) {
                chargeItemType = 2;
            } else if (10 == chargeItemType) {
                chargeItemType = 3;
            }
            // 收费项目为会员卡时，同步更新会员卡的价格
            mongoTemplate.updateFirst(query(where("vipCardType").is(chargeItemType)), update, VIPCard.class);
        }
        mongoTemplate.updateFirst(query(where("id").is(chargeItem.getId())), update, ChargeItem.class);
    }

    @Override
    public void insert(ChargeItem chargeItem) {
        chargeItem.setId(commonUtils.getNumCode());
        chargeItem.setCreateTime(new Date());
        chargeItem.setUpdateTime(new Date());
        mongoTemplate.save(chargeItem);
    }

}
