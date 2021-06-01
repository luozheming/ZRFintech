package com.service.manage.impl;

import com.pojo.Order;
import com.pojo.Project;
import com.pojo.ProjectComment;
import com.service.manage.ProjectCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
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
public class ProjectCommentServiceImpl implements ProjectCommentService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<ProjectComment> pageList(Integer pageNum, Integer pageSize) {
        int startNum = pageNum * pageSize;
        List<ProjectComment> projectComments = mongoTemplate.find(new Query().skip(startNum).limit(pageSize), ProjectComment.class);
        if (!CollectionUtils.isEmpty(projectComments)) {
            for(ProjectComment projectComment : projectComments) {
                if (null == projectComment.getStatus()) {
                    projectComment.setStatus(0);
                    if (projectComment.getIsDone()) {
                        projectComment.setStatus(1);
                    }
                    if (null != projectComment.getReplyTm()) {
                        projectComment.setStatus(2);
                    }
                }
                if (0 == projectComment.getStatus()) {
                    projectComment.setCommonStatus(0);
                } else if (1 == projectComment.getStatus() || 2 == projectComment.getStatus()
                        || 4 == projectComment.getStatus() || 6 == projectComment.getStatus()
                        || 7 == projectComment.getStatus() || 8 == projectComment.getStatus()) {
                    projectComment.setCommonStatus(2);
                } else if (3 == projectComment.getStatus() || 5 == projectComment.getStatus()) {
                    projectComment.setCommonStatus(1);
                }

                // 获取对应订单处理状态
                Order order = mongoTemplate.findOne(query(where("bizId").is(projectComment.getId())), Order.class);
                if (null != order) {
                    projectComment.setOrderPayStatus(order.getPayStatus());
                }
            }
        }
        return projectComments;
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
            update.set("status", 1);
            update.set("updateTime", new Date());
        }
        if (!StringUtils.isEmpty(projectComment.getReply())) {
            update.set("reply", projectComment.getReply());
            update.set("replyTm", new Date());
            update.set("status", 2);
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

    @Override
    public void status(String id, Integer status) {
        Update update = new Update();
        update.set("status", status);
        update.set("updateTime", new Date());
        mongoTemplate.updateFirst(query(where("id").is(id)), update, ProjectComment.class);
    }
}
