package com.service.manage.impl;

import com.dto.outdto.HomePageDto;
import com.pojo.EntUser;
import com.pojo.Project;
import com.pojo.User;
import com.service.manage.UserService;
import com.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
        // 所有的统计需排除内部人员操作的数据
        HomePageDto homePageDto = new HomePageDto();
        int entUserCount = (int) mongoTemplate.count(new Query(where("phoneNm").nin(internalPhoneNo.split(","))), "entuser");
        int isBrowseCount = (int) mongoTemplate.count(new Query(where("isBrowse").is(true).and("phoneNm").nin(internalPhoneNo.split(","))), "entuser");
        int isRoadShowBrowseCount = (int) mongoTemplate.count(new Query(where("isRoadShowBrowse").is(true).and("phoneNm").nin(internalPhoneNo.split(","))), "entuser");

        int bpCount = 0;
        int roadShowCount = 0;
        int isPayCount = 0;
        List<Project> projects = mongoTemplate.find(query(where("isDone").is(true)), Project.class);
        if (!CollectionUtils.isEmpty(projects)) {
            for (Project project : projects) {
                EntUser entUser = mongoTemplate.findOne(query(where("openId").is(project.getOpenId()).and("phoneNm").nin(internalPhoneNo.split(","))), EntUser.class);
                if (null != entUser) {
                    bpCount ++;

                    if (null != project.getIsPay() && project.getIsPay()) {
                        isPayCount ++;
                    }

                    if (null != project.getProjectType() && (2 == project.getProjectType() || 3 == project.getProjectType())) {
                        roadShowCount ++;
                    }
                }
            }
        }

        // 查询当天用户增长数
        String date = DateUtil.dateToStr(new Date(), "yyyy-MM-dd");
        Date dateStart = DateUtil.strToDate(date + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date dateEnd = DateUtil.strToDate(date + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        int entUserAddCountPerDay= (int) mongoTemplate.count(query(where("createTime").gt(dateStart).lt(dateEnd).and("phoneNm").nin(internalPhoneNo.split(","))), EntUser.class);
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
