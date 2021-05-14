package com.service.manage.impl;

import com.pojo.Project;
import com.pojo.ProjectComment;
import com.service.manage.ProjectCommentService;
import com.service.manage.ProjectService;
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

    @Autowired
    private ProjectCommentService projectCommentService;

    @Override
    public List<Project> pageList(Integer pageNum, Integer pageSize) {
        int startNum = pageNum * pageSize;
        List<Project> projects = mongoTemplate.find(new Query(where("isDone").is(true)).skip(startNum).limit(pageSize), Project.class);
        return projects;
    }

    @Override
    public Project detail(String projectNo) {
        Project project = mongoTemplate.findOne(query(where("projectNo").is(projectNo)), Project.class);
        List<ProjectComment> projectComments = projectCommentService.listByProjectNo(projectNo);
        project.setProjectCommentList(projectComments);
        return project;
    }

    @Override
    public Integer count() {
        return (int) mongoTemplate.count(new Query(where("isDone").is(true)),"project");
    }

    @Override
    public List<Project> listByEnt(String openId) {
        return mongoTemplate.find(query(where("openId").is(openId).and("isDone").is(true)), Project.class);
    }

    @Override
    public void delete(String projectNo) {
        mongoTemplate.remove(query(where("projectNo").is(projectNo)), Project.class);
    }
}
