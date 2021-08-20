package com.job;

import com.dto.indto.SendEmailDto;
import com.pojo.ProjectDeliver;
import com.service.EmailService;
import com.service.ProjectDeliverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * 项目投递定时任务；定时分批将项目bp发送至投资人电子邮箱
 */
@Configuration
@EnableScheduling
public class ProjectDeliverJob {

    @Autowired
    private ProjectDeliverService projectDeliverService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private MongoTemplate mongoTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ProjectDeliverJob.class);

    @Scheduled(cron = "0 */15 * * * ?")
    public void projectDeliver() {
        logger.info("项目投递定时任务开始...");
        int total = 0;
        try {
            List<ProjectDeliver> projectDeliverList = projectDeliverService.list();
            if (!CollectionUtils.isEmpty(projectDeliverList)) {
                total = projectDeliverList.size();
                BulkOperations operations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, ProjectDeliver.class);
                Update update = new Update();
                update.set("status", 1);
                SendEmailDto sendEmailDto = null;
                List<String> filePath = null;
                for (ProjectDeliver projectDeliver : projectDeliverList) {
                    sendEmailDto = new SendEmailDto();
                    filePath = new ArrayList<>();
                    // 随机设置邮件发送方
                    sendEmailDto.setReceiver(projectDeliver.getTargetEmail());
                    String theme = "项目推荐-【" + projectDeliver.getProjectNm() + "】";
                    sendEmailDto.setTheme(theme);// 邮件主题
                    sendEmailDto.setContent(getEmailContent(projectDeliver));// 邮件内容
                    if (!StringUtils.isEmpty(projectDeliver.getBpRoute())) {
                        filePath.add(projectDeliver.getBpRoute());
                    } else {
                        logger.info("项目编号|项目名称：" + projectDeliver.getProjectNo() + "|" + projectDeliver.getProjectNm() + "未上传BP");
                    }
                    sendEmailDto.setFilePathList(filePath);
                    try {
                        emailService.sendAttachmentsMail(sendEmailDto);
                    } catch (Exception e) {
                        update.set("status", 2);// 更新邮件发送失败
                    }

                    // 添加更新内容
                    update.set("updateDate", new Date());
                    Integer deliverTimes = projectDeliver.getDeliverTimes() == null ? 0 : projectDeliver.getDeliverTimes();
                    update.set("deliverTimes", deliverTimes + 1);
                    operations.updateOne(query(where("id").is(projectDeliver.getId())), update);
                }

                // 批量更新项目投递状态为成功
                operations.execute();
            }
            logger.info("项目投递定时任务结束，共投递邮件数：" + total);
        } catch (Exception e) {

            logger.error("项目投递定时任务异常：", e);
        }
    }

    /**
     * 获取邮件正文内容
     * @param projectDeliver
     * @return
     */
    private String getEmailContent(ProjectDeliver projectDeliver) {
        StringBuilder stringBuilder = new StringBuilder("尊敬的投资人，您好：\n");
        stringBuilder.append("\n");
        stringBuilder.append("[" + projectDeliver.getProjectNm() + "]融资信息如下：");
        stringBuilder.append("\n");
        stringBuilder.append("所属行业：").append(null == projectDeliver.getProIndus()?"未提供":projectDeliver.getProIndus()).append("\n");
        stringBuilder.append("融资轮次：").append(null == projectDeliver.getFinRound()?"未提供":projectDeliver.getFinRound()).append("\n");
        stringBuilder.append("融资金额：").append(null == projectDeliver.getQuota()?"未提供":projectDeliver.getQuota()).append("\n");
        BigDecimal sharesTransfer = projectDeliver.getSharesTransfer();
        if (null == sharesTransfer) {
            stringBuilder.append("出让股权：").append("未提供").append("\n");
        } else {
            stringBuilder.append("出让股权：").append(projectDeliver.getSharesTransfer()).append("%").append("\n");
        }
        stringBuilder.append("项目方联系人：").append(null == projectDeliver.getProUser()?"未提供":projectDeliver.getProUser()).append("\n");
        stringBuilder.append("项目方电话：").append(null == projectDeliver.getProPhonum()?"未提供":projectDeliver.getProPhonum()).append("\n");
        return stringBuilder.toString();
    }
}
