package com.service.manage.impl;

import com.dto.outdto.PageListDto;
import com.pojo.City;
import com.service.manage.CityService;
import com.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CommonUtils commonUtils;

    @Override
    public List<City> list(Integer cityType) {
        Query query = new Query();
        if (null != cityType) {
            query.addCriteria(where("cityType").is(cityType));
        }
        List<City> cities = mongoTemplate.find(query, City.class);
        return cities;
    }

    @Override
    public void edit(City city) {
        Update update = new Update();
        update.set("hotCity", city.getHotCity());
        update.set("city", city.getCity());
        update.set("updateTime", new Date());
        mongoTemplate.updateFirst(query(where("id").is(city.getId())), update, City.class);
    }

    @Override
    public void add(City city) {
        city.setId(commonUtils.getNumCode());
        city.setCreateTime(new Date());
        mongoTemplate.insert(city, "city");
    }
}
