package com.service.impl;

import com.pojo.Message;
import com.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Message> pageList(String userId, Integer pageNum, Integer pageSize) {
        int startNum = pageNum * pageSize;
        List<Message> messages = mongoTemplate.find(new Query(where("userId").is(userId))
                .with(Sort.by(Sort.Order.desc("createTime"))).skip(startNum).limit(pageSize), Message.class);
        return messages;
    }

    @Override
    public Integer count(String userId) {
        return (int) mongoTemplate.count(new Query(where("userId").is(userId)), "message");
    }

    @Override
    public void deleteMessage(String id) {
        mongoTemplate.remove(query(where("id").is(id)), Message.class);
    }
}
