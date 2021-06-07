package com.service.manage.impl;

import com.enums.CommentType;
import com.pojo.Project;
import com.pojo.ProjectBpApply;
import com.pojo.ProjectComment;
import com.service.manage.ProjectCommentService;
import com.service.manage.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
        if (!CollectionUtils.isEmpty(projects)) {
            for (Project project : projects) {
                List<Integer> commentTypes = mongoTemplate.findDistinct(query(where("projectNo").is(project.getProjectNo())), "commentType", "projectComment", Integer.class);
                String commentTypeDesc = "";
                if (!CollectionUtils.isEmpty(commentTypes)) {
                    for (Integer commentType : commentTypes)
                    commentTypeDesc = commentTypeDesc + "," + CommentType.getMessage(commentType);
                }
                ProjectBpApply projectBpApply = mongoTemplate.findOne(query(where("projectNo").is(project.getProjectNo())), ProjectBpApply.class);
                if (null != projectBpApply) {
                    commentTypeDesc = commentTypeDesc + "," + "定制商业计划书";
                }

                if (!StringUtils.isEmpty(commentTypeDesc)) {
                    commentTypeDesc = commentTypeDesc.substring(1);
                }
                project.setCommentTypeDesc(commentTypeDesc);
            }
        }
        return projects;
    }

    @Override
    public Project detail(String projectNo) {
        Project project = mongoTemplate.findOne(query(where("projectNo").is(projectNo)), Project.class);
        if (null != project) {
            List<ProjectComment> projectComments = projectCommentService.listByProjectNo(projectNo);
            project.setProjectCommentList(projectComments);
        }
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

    @Override
    public void status(String projectNo, Integer status) {
        Update update = new Update();
        update.set("status", status);
        mongoTemplate.updateFirst(query(where("projectNo").is(projectNo)), update, Project.class);
    }
}
