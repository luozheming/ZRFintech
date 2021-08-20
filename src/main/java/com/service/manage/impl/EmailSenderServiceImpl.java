package com.service.manage.impl;

import com.pojo.EmailSender;
import com.service.manage.EmailSenderService;
import com.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CommonUtils commonUtils;

    @Override
    public EmailSender detail(String id) {
        return mongoTemplate.findOne(query(where("id").is(id)), EmailSender.class);
    }

    @Override
    public List<EmailSender> list() {
        Query query = new Query();
        query.with(Sort.by(Sort.Order.asc("status")));
        return mongoTemplate.find(query, EmailSender.class);
    }

    @Override
    public void update(EmailSender emailSender) {
        Update update = new Update();
        if (!StringUtils.isEmpty(emailSender.getUserName())) {
            update.set("userName", emailSender.getUserName());
        }
        if (!StringUtils.isEmpty(emailSender.getPassword())) {
            update.set("password", emailSender.getPassword());
        }
        if (null != emailSender.getStatus()) {
            update.set("status", emailSender.getStatus());
        }
        mongoTemplate.updateFirst(query(where("id").is(emailSender.getId())), update, EmailSender.class);
    }

    @Override
    public void insert(EmailSender emailSender) {
        emailSender.setId(commonUtils.getNumCode());
        emailSender.setStatus(0);
        emailSender.setCreateTime(new Date());
        mongoTemplate.save(emailSender);
    }

    @Override
    public void status(EmailSender emailSender) {
        Update update = new Update();
        update.set("status", emailSender.getStatus());
        mongoTemplate.updateFirst(query(where("id").is(emailSender.getId())), update, EmailSender.class);
    }

}
