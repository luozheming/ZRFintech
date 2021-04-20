package com.service.impl;

import com.pojo.VIPCard;
import com.service.VIPCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class VIPCardServiceImpl implements VIPCardService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public VIPCard detail(String id) {
        return mongoTemplate.findOne(Query.query(where("id").is(id)), VIPCard.class);
    }

    @Override
    public List<VIPCard> list() {
        return mongoTemplate.find(new Query(), VIPCard.class);
    }

}
