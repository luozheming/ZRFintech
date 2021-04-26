package com.service.impl;

import com.pojo.ActivityRecord;
import com.service.ActivityRecordService;
import com.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class ActivityRecordServiceImpl implements ActivityRecordService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CommonUtils commonUtils;

    @Override
    public void add(ActivityRecord activityRecord) {
        activityRecord.setId(commonUtils.getNumCode());
        mongoTemplate.save(activityRecord);
    }

    @Override
    public ActivityRecord detailByPhoneNm(String phoneNm) {
        return mongoTemplate.findOne(query(where("participantPhoneNm").is(phoneNm)), ActivityRecord.class);
    }
}