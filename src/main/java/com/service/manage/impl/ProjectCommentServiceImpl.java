package com.service.manage.impl;

import com.pojo.ProjectComment;
import com.service.manage.ProjectCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class ProjectCommentServiceImpl implements ProjectCommentService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<ProjectComment> pageList(Integer pageNum, Integer pageSize) {
        int startNum = pageNum * pageSize;
        List<ProjectComment> ProjectComments = mongoTemplate.find(new Query().skip(startNum).limit(pageSize), ProjectComment.class);
        return ProjectComments;
    }

    @Override
    public void commit(ProjectComment projectComment) {
        Update update = new Update();
        if (null != projectComment.getFavor()) {
            update.set("favor", projectComment.getFavor());
        }
        if (!StringUtils.isEmpty(projectComment.getContent())) {
            update.set("isDone", true);
            update.set("content", projectComment.getContent());
            update.set("updateTm", new Date());
        }
        if (!StringUtils.isEmpty(projectComment.getReply())) {
            update.set("reply", projectComment.getReply());
            update.set("replyTm", new Date());
        }
        Criteria criteria = where("id").is(projectComment.getId());
        mongoTemplate.updateFirst(query(criteria), update, ProjectComment.class);
    }

    @Override
    public List<ProjectComment> listByProjectNo(String projectNo) {
        Criteria criteria = where("projectNo").is(projectNo);
        List<ProjectComment> projectComments = mongoTemplate.find(query(criteria), ProjectComment.class);
        return projectComments;
    }

    @Override
    public Integer count() {
        return (int) mongoTemplate.count(new Query(),"projectComment");
    }

    @Override
    public void delete(String id) {
        mongoTemplate.remove(query(where("id").is(id)), ProjectComment.class);
    }
}
