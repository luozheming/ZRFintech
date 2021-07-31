package com.service;

import com.pojo.ActivityRecord;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.util.List;

public interface ActivityRecordService {
    void add(ActivityRecord activityRecord);
    ActivityRecord detailByPhoneNm(String phoneNm, String activityId);
    ActivityRecord detailByUserId(String userId, String activityId);
    List<ActivityRecord> pageList(Integer pageNum, Integer pageSize, Integer activityType, String userId);
    Integer count(Integer activityType, String userId);
    void delete(String id);
}
