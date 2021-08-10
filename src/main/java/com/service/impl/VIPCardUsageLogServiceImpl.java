package com.service.impl;

import com.dto.indto.SendEmailDto;
import com.pojo.*;
import com.service.EmailService;
import com.service.VIPCardUsageLogService;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
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
    @Autowired
    private EmailService emailService;
    @Value("${project.deliver.email}")
    private String email;
    @Value("${platform.adviser.email}")
    private String adviserEmail;

    @Override
    public List<VIPCardUsageLog> list(String vipCardUsageId) {
        return mongoTemplate.find(query(where("vipCardUsageId").is(vipCardUsageId)), VIPCardUsageLog.class);
    }

    @Override
    public void add(VIPCardUsageLog vipCardUsageLog) throws Exception {
        // 获取用户的项目信息
        Project project = mongoTemplate.findOne(query(where("entUserId").is(vipCardUsageLog.getUserId())), Project.class);
        if (null == project) {
            throw new Exception(ErrorCode.PROJECTEMPTY.getMessage());
        }
        if (StringUtils.isEmpty(project.getBpRoute())) {
            throw new Exception(ErrorCode.BPROUTEEMPTY.getMessage());
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
                    BeanUtils.copyProperties(project, projectDeliver);
                    projectDeliver.setId(commonUtils.getNumCode());
                    projectDeliver.setUserId(vipCardUsageLog.getUserId());
                    projectDeliver.setVipCardUsageLogId(vipCardUsageLogId);
                    projectDeliver.setTargetType(1);// 项目投递目标：1-VC,2-FA
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

    @Override
    public void sendMailToAdviser(String userId, String telephoneNo) throws Exception {
        // 获取用户信息
        User user = mongoTemplate.findOne(query(where("userId").is(userId)), User.class);
        if (null == user) {
            throw new Exception(ErrorCode.EMPITYUSER.getMessage());
        }
        // 获取用户的项目信息
        Project project = mongoTemplate.findOne(query(where("entUserId").is(user.getUserId())), Project.class);
        SendEmailDto sendEmailDto = new SendEmailDto();
        List<String> emails = Arrays.asList(email.split(","));
        sendEmailDto.setSender(emails.get(0));
        sendEmailDto.setReceiver(adviserEmail);
        sendEmailDto.setTheme("金卡顾问服务申请");
        StringBuilder stringBuilder = new StringBuilder("客户信息：");
        stringBuilder.append("\n");
        stringBuilder.append("姓名：").append(null == user.getUserName()?"":user.getUserName()).append("\n");
        stringBuilder.append("手机：").append(null == user.getPhoneNm()?"":user.getPhoneNm()).append("\n");
        stringBuilder.append("联系电话：").append(null == telephoneNo?"":telephoneNo).append("\n");
        List<String> filePath = new ArrayList<>();
        if (null != project) {
            if (!StringUtils.isEmpty(project.getBpRoute())) {
                filePath.add(project.getBpRoute());
                sendEmailDto.setFilePathList(filePath);
            }
            stringBuilder.append("项目信息：").append("\n");
            stringBuilder.append("所属行业：").append(null == project.getProIndus()?"未提供":project.getProIndus()).append("\n");
            stringBuilder.append("融资轮次：").append(null == project.getFinRound()?"未提供":project.getFinRound()).append("\n");
            stringBuilder.append("融资金额：").append(null == project.getQuota()?"未提供":project.getQuota()).append("\n");
            stringBuilder.append("出让股权：").append(null == project.getSharesTransfer()?"未提供":project.getSharesTransfer()).append("\n");
            stringBuilder.append("项目方联系人：").append(null == project.getProUser()?"未提供":project.getProUser()).append("\n");
            stringBuilder.append("项目方电话：").append(null == project.getProPhonum()?"未提供":project.getProPhonum()).append("\n");
        }
        sendEmailDto.setContent(stringBuilder.toString());
        emailService.sendAttachmentsMail(sendEmailDto);
    }
}