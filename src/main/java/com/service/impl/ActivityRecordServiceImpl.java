package com.service.impl;

import com.pojo.ActivityRecord;
import com.pojo.Project;
import com.service.ActivityRecordService;
import com.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

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
        mongoTemplate.save(activityRecord);
    }

    @Override
    public ActivityRecord detailByPhoneNm(String phoneNm) {
        return mongoTemplate.findOne(query(where("participantPhoneNm").is(phoneNm)), ActivityRecord.class);
    }

    @Override
    public List<ActivityRecord> pageList(Integer pageNum, Integer pageSize, Integer activityType) {
        int startNum = pageNum * pageSize;
        Query query = new Query();
        if (null != activityType) {
            query.addCriteria(where("activityType").is(activityType));
        }
        List<ActivityRecord> activityRecords = mongoTemplate.find(query.skip(startNum).limit(pageSize), ActivityRecord.class);
        if (1 == activityType) {
            for (ActivityRecord activityRecord : activityRecords) {
                Project project = mongoTemplate.findOne(query(where("projectNo").is(activityRecord.getProjectNo())), Project.class);
                if (null != project) {
                    activityRecord.setProjectStatus(project.getStatus());
                }
            }
        }
        return activityRecords;
    }

    @Override
    public Integer count(Integer activityType) {
        Query query = new Query();
        if (null != activityType) {
            query.addCriteria(where("activityType").is(activityType));
        }
        return (int) mongoTemplate.count(query,"activityRecord");
    }

    @Override
    public void delete(String id) {
        mongoTemplate.remove(query(where("id").is(id)), ActivityRecord.class);
    }
}
