package com.service;

import com.pojo.ActivityRecord;

public interface ActivityRecordService {
    void add(ActivityRecord activityRecord);
    ActivityRecord detailByPhoneNm(String phoneNm);
}
