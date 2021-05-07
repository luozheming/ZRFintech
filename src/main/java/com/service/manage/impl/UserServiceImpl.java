package com.service.manage.impl;

import com.dto.outdto.HomePageDto;
import com.pojo.User;
import com.service.manage.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public User login(String phoneNm, String password) {
        User user = mongoTemplate.findOne(query(where("phoneNm").is(phoneNm).and("password").is(password)), User.class);
        return  user;
    }

    @Override
    public User getById(String userId) {
        User user = mongoTemplate.findOne(query(where("userId").is(userId)), User.class);
        return  user;
    }

    @Override
    public HomePageDto homePage() {
        HomePageDto homePageDto = new HomePageDto();
        int entUserCount = (int) mongoTemplate.count(new Query(), "entuser");
        int isBrowseCount = (int) mongoTemplate.count(new Query(where("isBrowse").is(true)), "entuser");
        int bpCount = (int) mongoTemplate.count(new Query(where("isDone").is(true)), "project");
        int isPayCount = (int) mongoTemplate.count(new Query(where("isPay").is(true)), "project");
        homePageDto.setEntUserCount(entUserCount);
        homePageDto.setIsBrowseCount(isBrowseCount);
        homePageDto.setBpCount(bpCount);
        homePageDto.setIsPayCount(isPayCount);
        return homePageDto;
    }

}
