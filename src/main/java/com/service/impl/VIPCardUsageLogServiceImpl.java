package com.service.impl;

import com.pojo.Investor;
import com.pojo.Project;
import com.pojo.ProjectDeliver;
import com.pojo.VIPCardUsageLog;
import com.service.VIPCardUsageLogService;
import com.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class VIPCardUsageLogServiceImpl implements VIPCardUsageLogService {

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<VIPCardUsageLog> list(String vipCardUsageId) {
        return mongoTemplate.find(query(where("vipCardUsageId").is(vipCardUsageId)), VIPCardUsageLog.class);
    }

    @Override
    public void add(VIPCardUsageLog vipCardUsageLog) {
        // 获取用户的项目信息
        Project project = mongoTemplate.findOne(query(where("entUserId").is(vipCardUsageLog.getUserId())), Project.class);
        if (null == project) {
            return;
        }
        String vipCardUsageLogId = commonUtils.getNumCode();
        vipCardUsageLog.setId(vipCardUsageLogId);
        vipCardUsageLog.setCreateTime(new Date());
        if (!CollectionUtils.isEmpty(vipCardUsageLog.getInvestorIdList())) {
            vipCardUsageLog.setUsageTimes(vipCardUsageLog.getInvestorIdList().size());
        }
        mongoTemplate.save(vipCardUsageLog);

        if (!CollectionUtils.isEmpty(vipCardUsageLog.getInvestorIdList())) {
            List<Investor> investors = mongoTemplate.find(query(where("investorId").in(vipCardUsageLog.getInvestorIdList())), Investor.class);
            if (!CollectionUtils.isEmpty(investors)) {
                List<ProjectDeliver> projectDeliverList = new ArrayList<>();
                ProjectDeliver projectDeliver =  null;
                for (Investor investor : investors) {
                    projectDeliver = new ProjectDeliver();
                    projectDeliver.setId(commonUtils.getNumCode());
                    projectDeliver.setUserId(vipCardUsageLog.getUserId());
                    projectDeliver.setVipCardUsageLogId(vipCardUsageLogId);
                    projectDeliver.setTargetType(1);// 项目投递目标：1-VC,2-FA
                    projectDeliver.setProjectNo(project.getProjectNo());
                    projectDeliver.setBpRoute(project.getBpRoute());
                    projectDeliver.setStatus(0);
                    projectDeliver.setTargetEmail(investor.getInvesEmail());
                    projectDeliver.setTargetObject(investor);
                    projectDeliver.setCreateDate(new Date());
                    projectDeliverList.add(projectDeliver);
                }
                mongoTemplate.insert(projectDeliverList, ProjectDeliver.class);
            }
        }
    }
}