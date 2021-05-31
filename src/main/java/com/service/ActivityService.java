package com.service;

import com.pojo.Activity;
import com.pojo.ActivityRecord;

import java.util.List;

public interface ActivityService {
    void add(Activity activity);
    void edit(Activity activity);
    List<Activity> pageList(Integer pageNum, Integer pageSize, Integer activityType);
    Integer count(Integer activityType);
    Activity detail(String id);
    void delete(String id);
    Activity draft();
}
