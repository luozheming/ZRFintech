package com.service.impl;

import com.pojo.Activity;
import com.service.ActivityService;
import com.utils.CommonUtils;
import com.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void add(Activity activity) {
        if (StringUtils.isEmpty(activity.getId())) {
            activity.setId(commonUtils.getNumCode());
        }
        if (null != activity.getIsDone() && !activity.getIsDone()) {
            // 删除历史活动草稿，只保留最新草稿
            mongoTemplate.remove(query(where("idDone").is(false)), Activity.class);
        }
        activity.setStatus(1);
        activity.setCreateTime(new Date());
        mongoTemplate.save(activity);
    }

    @Override
    public void edit(Activity activity) {
        Update update = new Update();
        if (!StringUtils.isEmpty(activity.getPhotoRoute())) {
            update.set("photoRoute", activity.getPhotoRoute());
        }
        update.set("theme", activity.getTheme());
        update.set("startDate", activity.getStartDate());
        update.set("endDate", activity.getEndDate());
        update.set("activityMode", activity.getActivityMode());
        update.set("activityContent", activity.getActivityContent());
        update.set("price", activity.getPrice());
        update.set("disCountPrice", activity.getDisCountPrice());
        update.set("updateTime", new Date());
        mongoTemplate.updateFirst(query(where("id").is(activity.getId())), update, Activity.class);
    }

    @Override
    public List<Activity> pageList(Integer pageNum, Integer pageSize, Integer activityType) {
        int startNum = pageNum * pageSize;
        Query query = new Query();
        if (null != activityType) {
            query = query.addCriteria(where("activityType").is(activityType));
        }
        query.with(Sort.by(Sort.Order.desc("endDate")));
        List<Activity> activities = mongoTemplate.find(query.skip(startNum).limit(pageSize), Activity.class);
        if (!CollectionUtils.isEmpty(activities)) {
            for (Activity activity : activities) {
                if (!StringUtils.isEmpty(activity.getEndDate()) && DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss").compareTo(activity.getEndDate()) > 0) {
                    activity.setStatus(2);
                }
                if (!StringUtils.isEmpty(activity.getPhotoRoute())) {
                    activity.setPhoto(commonUtils.getPhoto(activity.getPhotoRoute()));
                }
            }
        }
        return activities;
    }

    @Override
    public Integer count(Integer activityType) {
        Query query = new Query();
        if (null != activityType) {
            query = query.addCriteria(where("activityType").is(activityType));
        }
        Integer count = (int) mongoTemplate.count(query, Activity.class);
        return count;
    }

    @Override
    public Activity detail(String id) {
        Activity activity = mongoTemplate.findOne(query(where("id").is(id)), Activity.class);
        if (!StringUtils.isEmpty(activity.getPhotoRoute())) {
            activity.setPhoto(commonUtils.getPhoto(activity.getPhotoRoute()));
        }
        return activity;
    }

    @Override
    public void delete(String id) {
        mongoTemplate.remove(query(where("id").is(id)), Activity.class);
    }

    @Override
    public Activity draft() {
        Activity activity = mongoTemplate.findOne(query(where("isDone").is(false)), Activity.class);
        if (null != activity && !StringUtils.isEmpty(activity.getPhotoRoute())) {
            activity.setPhoto(commonUtils.getPhoto(activity.getPhotoRoute()));
        }
        return activity;
    }
}
