package com.service.manage.impl;

import com.pojo.ProjectBpApply;
import com.service.manage.ProjectBpApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class ProjectBpApplyServiceImpl implements ProjectBpApplyService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<ProjectBpApply> pageListByEnt(Integer pageNum, Integer pageSize, String openId) {
        int startNum = pageNum * pageSize;
        List<ProjectBpApply> projectBpApplyList = mongoTemplate.find(new Query(where("openId").is(openId)).skip(startNum).limit(pageSize), ProjectBpApply.class);
        return projectBpApplyList;
    }

    @Override
    public Integer countByEnt(String openId) {
        return (int) mongoTemplate.count(new Query(where("openId").is(openId)),"projectBpApply");
    }
}
