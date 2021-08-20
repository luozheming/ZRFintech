package com.service.impl;

import com.pojo.VIPCard;
import com.service.VIPCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class VIPCardServiceImpl implements VIPCardService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public VIPCard detail(String id) {
        return mongoTemplate.findOne(query(where("id").is(id)), VIPCard.class);
    }

    @Override
    public List<VIPCard> list() {
        Query query = new Query();
        query.with(Sort.by(Sort.Order.asc("orderNo")));
        return mongoTemplate.find(query, VIPCard.class);
    }

    @Override
    public void update(VIPCard vipCard) {
        Update update = new Update();
        if (null != vipCard.getPrice()) {
            update.set("price", vipCard.getPrice());
        }
        if (null != vipCard.getDiscountPrice()) {
            update.set("discountPrice", vipCard.getDiscountPrice());
        }
        mongoTemplate.updateFirst(query(where("id").is(vipCard.getId())), update, VIPCard.class);
    }

}
