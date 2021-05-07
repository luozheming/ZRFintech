package com.service.manage.impl;

import com.pojo.UsualAddress;
import com.service.manage.UsualAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class UsualAddressServiceImpl implements UsualAddressService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<UsualAddress> listByUserId(String userId) {
        return mongoTemplate.find(query(where("userId").is(userId)), UsualAddress.class);
    }

    @Override
    public void edit(UsualAddress usualAddress) {
        Update update = new Update();
        update.set("location", usualAddress.getLocation());
        update.set("receiverName", usualAddress.getReceiverName());
        update.set("receiverPhoneNm", usualAddress.getReceiverPhoneNm());
        update.set("detailAddress", usualAddress.getDetailAddress());
        update.set("zipCode", usualAddress.getZipCode());
        mongoTemplate.updateFirst(query(where("id").is(usualAddress.getId())), update, UsualAddress.class);
    }
}
