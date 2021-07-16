package com.service.manage.impl;

import com.pojo.CarouselPicture;
import com.service.manage.CarouselPictureService;
import com.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class CarouselPictureServiceImpl implements CarouselPictureService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CommonUtils commonUtils;

    @Override
    public List<CarouselPicture> list(Integer photoType, Integer status, String pageLocation) {
        Query query = new Query();
        if (null != status) {
            query = query(where("status").is(status));
        }
        if (null != photoType) {
            query = query.addCriteria(where("photoType").is(photoType));
        }
        if (!StringUtils.isEmpty(pageLocation)) {
            query = query.addCriteria(where("pageLocation").is(pageLocation));
        }
        List<Sort.Order> orderList = new ArrayList<Sort.Order>();
//        Sort sort = Sort.by(Sort.Order.desc(""));
        List<CarouselPicture> carouselPictures = mongoTemplate.find(query.with(Sort.by(Sort.Order.asc("orderNo"))), CarouselPicture.class);
        if (!CollectionUtils.isEmpty(carouselPictures)) {
            for (CarouselPicture carouselPicture : carouselPictures) {
                if (!StringUtils.isEmpty(carouselPicture.getPhotoRoute())) {
                    carouselPicture.setPhotoRoute(commonUtils.getFullFilePath(carouselPicture.getPhotoRoute()));
                }
            }
        }
        return carouselPictures;
    }

    @Override
    public void add(CarouselPicture carouselPicture) {
        if (StringUtils.isEmpty(carouselPicture.getId())) {
            carouselPicture.setId(commonUtils.getNumCode());
        }
        carouselPicture.setStatus(0);
        mongoTemplate.save(carouselPicture);
    }

    @Override
    public void status(String id, Integer status) {
        Update update = new Update();
        update.set("status", status);
        mongoTemplate.updateFirst(query(where("id").is(id)), update, CarouselPicture.class);
    }

    @Override
    public void edit(CarouselPicture carouselPicture) {
        Update update = new Update();
        if (!StringUtils.isEmpty(carouselPicture.getPhotoRoute())) {
            update.set("photoRoute", carouselPicture.getPhotoRoute());
        }
        update.set("linkUrl", carouselPicture.getLinkUrl());
        update.set("photoType", carouselPicture.getPhotoType());
        update.set("orderNo", carouselPicture.getOrderNo());
        update.set("pageLocation", carouselPicture.getPageLocation());
        mongoTemplate.updateFirst(query(where("id").is(carouselPicture.getId())), update, CarouselPicture.class);
    }

    @Override
    public CarouselPicture detail(String id) {
        CarouselPicture carouselPicture = mongoTemplate.findOne(query(where("id").is(id)), CarouselPicture.class);
        carouselPicture.setPhotoRoute(commonUtils.getFullFilePath(carouselPicture.getPhotoRoute()));
        return carouselPicture;
    }

    @Override
    public List<String> pageLocationList(Integer photoType) {
        List<String> distinct = mongoTemplate.findDistinct(query(where("photoType").is(photoType)), "pageLocation", "carouselPicture", CarouselPicture.class, String.class);
        return distinct;
    }
}
