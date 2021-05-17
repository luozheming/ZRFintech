package com.service.impl;

import com.pojo.Message;
import com.service.MessageService;
import com.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CommonUtils commonUtils;

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
    public void deleteMessage(String userId, String id) {
        if (!StringUtils.isEmpty(userId)) {
            // 清空用户的消息
            mongoTemplate.remove(query(where("userId").is(userId)), Message.class);
        } else {
            // 删除一条用户的消息
            mongoTemplate.remove(query(where("id").is(id)), Message.class);
        }
    }

    @Override
    public void readMessage(String userId) {
        Update update = new Update();
        update.set("status", 1);
        mongoTemplate.updateMulti(query(where("userId").is(userId).and("status").is(0)), update, Message.class);
    }

    @Override
    public void add(Message message) {
        if (StringUtils.isEmpty(message.getId())) {
            message.setId(commonUtils.getNumCode());
        }
        mongoTemplate.save(message);
    }
}
