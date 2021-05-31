package com.service;

import com.pojo.ActivityRecord;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.util.List;

public interface ActivityRecordService {
    void add(ActivityRecord activityRecord);
    ActivityRecord detailByPhoneNm(String phoneNm);
    List<ActivityRecord> pageList(Integer pageNum, Integer pageSize, Integer activityType);
    Integer count(Integer activityType);
    void delete(String id);
}
