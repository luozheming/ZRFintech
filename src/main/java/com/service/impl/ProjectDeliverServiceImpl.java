package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.pojo.Project;
import com.pojo.ProjectDeliver;
import com.service.ProjectDeliverService;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class ProjectDeliverServiceImpl implements ProjectDeliverService {

    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    private MongoTemplate mongoTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ProjectDeliverServiceImpl.class);

    @Override
    public void add(ProjectDeliver projectDeliver) throws Exception {
        logger.info("单笔bp投递入参：" + JSONObject.toJSONString(projectDeliver));
        if (StringUtils.isEmpty(projectDeliver.getUserId())) {
            throw new Exception("userId为空");
        }
        // 获取用户的项目信息
        Project project = mongoTemplate.findOne(query(where("entUserId").is(projectDeliver.getUserId())), Project.class);
        if (null == project) {
            throw new Exception(ErrorCode.PROJECTEMPTY.getMessage());
        }
        if (StringUtils.isEmpty(project.getBpRoute())) {
            throw new Exception(ErrorCode.BPROUTEEMPTY.getMessage());
        }
        BeanUtils.copyProperties(project, projectDeliver);
        projectDeliver.setId(commonUtils.getNumCode());
        projectDeliver.setCreateDate(new Date());
        projectDeliver.setStatus(0);
        mongoTemplate.save(projectDeliver);
    }

    @Override
    public void add(List<ProjectDeliver> projectDeliverList) {
        mongoTemplate.save(projectDeliverList);
    }

    @Override
    public List<ProjectDeliver> list() {
        return mongoTemplate.find(query(where("status").is(0)).limit(50).with(Sort.by(Sort.Order.asc("createTime"))), ProjectDeliver.class);
    }

    @Override
    public List<ProjectDeliver> pageListByUserId(Integer pageNum, Integer pageSize, String userId) {
        int startNum = pageNum * pageSize;
        Query query = new Query(where("userId").is(userId));
        query.with(Sort.by(Sort.Order.desc("createTime")));
        List<ProjectDeliver> projectDeliverList = mongoTemplate.find(query.skip(startNum).limit(pageSize), ProjectDeliver.class);
        return projectDeliverList;
    }

    @Override
    public Integer count(String userId) {
        return (int) mongoTemplate.count(query(where("userId").is(userId)), ProjectDeliver.class);
    }

}
