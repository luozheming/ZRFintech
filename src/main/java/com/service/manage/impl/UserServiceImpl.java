package com.service.manage.impl;

import com.dto.outdto.HomePageDto;
import com.pojo.EntUser;
import com.pojo.User;
import com.service.manage.UserService;
import com.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Value("${internalPhoneNo}")
    private String internalPhoneNo;

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
        int entUserCount = (int) mongoTemplate.count(new Query(where("phoneNm").nin(internalPhoneNo.split(","))), "entuser");
        int isBrowseCount = (int) mongoTemplate.count(new Query(where("isBrowse").is(true)), "entuser");
        int isRoadShowBrowseCount = (int) mongoTemplate.count(new Query(where("isRoadShowBrowse").is(true)), "entuser");
        int bpCount = (int) mongoTemplate.count(new Query(where("isDone").is(true).and("projectType").is(1)), "project");
        List<Integer> projectTypeList = new ArrayList<>();
        projectTypeList.add(2);
        projectTypeList.add(3);
        int roadShowCount = (int) mongoTemplate.count(new Query(where("isDone").is(true).and("projectType").in(projectTypeList)), "project");
        int isPayCount = (int) mongoTemplate.count(new Query(where("isPay").is(true)), "project");

        // 查询当天用户增长数
        String date = DateUtil.getFormatDate(new Date(), "yyyy-MM-dd");
        String dateStart = date + " 00:00:00";
        String dateEnd = date + " 23:59:59";
        int entUserAddCountPerDay= (int) mongoTemplate.count(query(where("createTime").lt(dateEnd).gt(dateStart)), EntUser.class);
        homePageDto.setEntUserCount(entUserCount);
        homePageDto.setIsBrowseCount(isBrowseCount);
        homePageDto.setIsRoadShowBrowseCount(isRoadShowBrowseCount);
        homePageDto.setBpCount(bpCount);
        homePageDto.setRoadShowCount(roadShowCount);
        homePageDto.setIsPayCount(isPayCount);
        homePageDto.setEntUserAddCountPerDay(entUserAddCountPerDay);
        return homePageDto;
    }

}
