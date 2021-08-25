package com.service.impl;

import com.pojo.Activity;
import com.pojo.ActivityRecord;
import com.pojo.Project;
import com.service.ActivityRecordService;
import com.utils.CommonUtils;
import com.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

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
        activityRecord.setCreateTime(new Date());
        Activity activity = mongoTemplate.findOne(query(where("id").is(activityRecord.getActivityId())), Activity.class);
        if (null != activity) {
            activityRecord.setSubTheme(activity.getSubTheme());
            activityRecord.setCity(activity.getCity());
        }
        mongoTemplate.save(activityRecord);
    }

    @Override
    public ActivityRecord detailByPhoneNm(String phoneNm, String activityId) {
        return mongoTemplate.findOne(query(where("participantPhoneNm").is(phoneNm).and("activityId").is(activityId)), ActivityRecord.class);
    }

    @Override
    public ActivityRecord detailByUserId(String userId, String activityId) {
        return mongoTemplate.findOne(query(where("userId").is(userId).and("activityId").is(activityId)), ActivityRecord.class);
    }

    @Override
    public List<ActivityRecord> pageList(Integer pageNum, Integer pageSize, Integer activityType, String userId) {
        int startNum = pageNum * pageSize;
        Query query = new Query();
        if (null != activityType) {
            query.addCriteria(where("activityType").is(activityType));
        }
        if (null != userId) {
            query.addCriteria(where("userId").is(userId));
        }
        List<ActivityRecord> activityRecords = mongoTemplate.find(query.skip(startNum).limit(pageSize), ActivityRecord.class);
        for (ActivityRecord activityRecord : activityRecords) {
            if (!StringUtils.isEmpty(activityRecord.getEndDate()) && DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss").compareTo(activityRecord.getEndDate()) > 0) {
                activityRecord.setActivityStatus(2);
            } else {
                activityRecord.setActivityStatus(1);
            }
        }
        return activityRecords;
    }

    @Override
    public Integer count(Integer activityType, String userId) {
        Query query = new Query();
        if (null != activityType) {
            query.addCriteria(where("activityType").is(activityType));
        }
        if (null != userId) {
            query.addCriteria(where("userId").is(userId));
        }
        return (int) mongoTemplate.count(query,"activityRecord");
    }

    @Override
    public void delete(String id) {
        mongoTemplate.remove(query(where("id").is(id)), ActivityRecord.class);
    }
}
