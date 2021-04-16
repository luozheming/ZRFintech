package com.service.manage.impl;

import com.pojo.EntUser;
import com.service.manage.EntUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntUserServiceImpl implements EntUserService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<EntUser> pageList(Integer pageNum, Integer pageSize) {
        int startNum = pageNum * pageSize;
        List<EntUser> entUsers = mongoTemplate.find(new Query().skip(startNum).limit(pageSize), EntUser.class);
        return entUsers;
    }

    @Override
    public Integer count() {
        return (int) mongoTemplate.count(new Query(),"entuser");
    }
}
