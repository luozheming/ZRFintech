package com.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.PageListDto;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.pojo.*;
import com.service.ProjectDeliverService;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
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
        Project project = mongoTemplate.findOne(query(where("entUserId").is(projectDeliver.getUserId())).with(Sort.by(Sort.Order.asc("createTime"))), Project.class);
        logger.info("单笔bp投递，获取项目信息：" + JSONObject.toJSONString(project));
        if (null == project) {
            throw new Exception(ErrorCode.PROJECTEMPTY.getMessage());
        }
        if (StringUtils.isEmpty(project.getBpRoute())) {
            throw new Exception(ErrorCode.BPROUTEEMPTY.getMessage());
        }
        BeanUtils.copyProperties(project, projectDeliver);
        projectDeliver.setId(commonUtils.getNumCode());
        projectDeliver.setCreateDate(new Date());
        projectDeliver.setUpdateDate(new Date());
        projectDeliver.setStatus(0);
        projectDeliver.setDeliverTimes(0);
        // 项目投递目标：1-VC,2-FA
        if (1 == projectDeliver.getTargetType()) {
            Investor investor = JSONObject.parseObject(JSON.toJSONString(projectDeliver.getTargetObject()), Investor.class);
            projectDeliver.setTargetUserId(investor.getInvestorId());
        } else if (2 == projectDeliver.getTargetType()) {
            FinancialAdvisor financialAdvisor = JSONObject.parseObject(JSON.toJSONString(projectDeliver.getTargetObject()), FinancialAdvisor.class);
            projectDeliver.setTargetUserId(financialAdvisor.getFaId());
        }
        mongoTemplate.save(projectDeliver);
    }

    @Override
    public void add(List<ProjectDeliver> projectDeliverList) {
        mongoTemplate.save(projectDeliverList);
    }

    @Override
    public List<ProjectDeliver> list() {
        // 获取未发送或者发送失败的邮件
        Query query = new Query();
        Criteria statusCriteria = new Criteria();
        // 投递状态：0-录入成功未投递，1-定时投递成功，2-失败,投递失败允许再投2次
        statusCriteria.orOperator(where("status").is(0), where("status").is(2));
        Criteria deliverTimesCriteria = new Criteria();
        deliverTimesCriteria.orOperator(where("deliverTimes").lt(3), where("deliverTimes").is(null));
        query.addCriteria(new Criteria().andOperator(statusCriteria, deliverTimesCriteria));
        return mongoTemplate.find(query.with(Sort.by(Sort.Order.asc("updateDate"))).limit(50), ProjectDeliver.class);
    }

    @Override
    public List<ProjectDeliver> pageListByUserId(Integer pageNum, Integer pageSize, String userId) {
        int startNum = pageNum * pageSize;
        Query query = new Query(where("userId").is(userId)).with(Sort.by(Sort.Order.desc("createDate")));
        List<ProjectDeliver> projectDeliverList = mongoTemplate.find(query.skip(startNum).limit(pageSize), ProjectDeliver.class);
        return projectDeliverList;
    }

    @Override
    public Integer count(String userId) {
        return (int) mongoTemplate.count(query(where("userId").is(userId)), ProjectDeliver.class);
    }

    @Override
    public PageListDto<ProjectDeliver> pageListByTargetUserId(Integer pageNum, Integer pageSize, String targetUserId) {
        Query query = new Query();
        query.addCriteria(where("targetUserId").is(targetUserId));
        int count = (int) mongoTemplate.count(query, ProjectDeliver.class);
        int totalPage = count/pageSize;
        PageListDto pageListDto = new PageListDto<Investor>();
        pageListDto.setTotal(count);
        if(pageNum <= totalPage){
            int startNum = pageNum * pageSize;
            List<ProjectDeliver> projectDelivers = mongoTemplate.find(query.skip(startNum).limit(pageSize), ProjectDeliver.class);
            pageListDto.setList(projectDelivers);
        }
        return pageListDto;
    }

}
