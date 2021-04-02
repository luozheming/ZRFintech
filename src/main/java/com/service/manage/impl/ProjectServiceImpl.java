package com.service.manage.impl;

import com.pojo.Project;
import com.service.manage.ProjectService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Project> pageList(Integer pageNum, Integer pageSize) {
        int startNum = pageNum * pageSize;
        List<Project> projects = mongoTemplate.find(new Query().skip(startNum).limit(pageSize), Project.class);
        return projects;
    }

    @Override
    public Project detail(String projectNo) {
        Project project = mongoTemplate.findOne(query(where("projectNo").is("")), Project.class);
        return project;
    }
}
