package com.service.manage.impl;

import com.pojo.ProjectComment;
import com.service.manage.ProjectCommentService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

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
        update.set("isDone", true);
        if (null != projectComment.getFavor()) {
            update.set("favor", projectComment.getFavor());
        }
        if (!StringUtils.isEmpty(projectComment.getContent())) {
            update.set("content", projectComment.getContent());
        }
        update.set("updateTm", new Date());
        Criteria criteria = Criteria.where("projectNo").is(projectComment.getProjectNo());
        mongoTemplate.updateFirst(query(criteria), update, ProjectComment.class);
    }
}
