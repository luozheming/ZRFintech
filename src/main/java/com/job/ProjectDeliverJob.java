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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * 项目投递定时任务；定时分批将项目bp发送至投资人电子邮箱
 */
@Configuration
@EnableScheduling
public class ProjectDeliverJob {

    @Value("${project.deliver.email}")
    private String email;
    @Value("${project.deliver.theme}")
    private String theme;
    @Value("${project.deliver.content}")
    private String content;
    @Autowired
    private ProjectDeliverService projectDeliverService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private MongoTemplate mongoTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ProjectDeliverJob.class);

    @Scheduled(cron = "0 */10 * * * ?")
    public void projectDeliver() {
        logger.info("项目投递定时任务开始...");
        int total = 0;
        try {
            List<ProjectDeliver> projectDeliverList = projectDeliverService.list();
            if (!CollectionUtils.isEmpty(projectDeliverList)) {
                total = projectDeliverList.size();
                List<String> emails = Arrays.asList(email.split(","));
                BulkOperations operations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, ProjectDeliver.class);
                Update update = new Update();
                update.set("status", 1);
                SendEmailDto sendEmailDto = null;
                List<String> filePath = new ArrayList<>();
                for (ProjectDeliver projectDeliver : projectDeliverList) {
                    sendEmailDto = new SendEmailDto();
                    // 随机设置邮件发送方
                    sendEmailDto.setSender(emails.get(new Random().nextInt(emails.size()-1)));
                    sendEmailDto.setReceiver(projectDeliver.getTargetEmail());
                    sendEmailDto.setTheme(theme);
                    sendEmailDto.setContent(content);
                    filePath.add(projectDeliver.getBpRoute());
                    sendEmailDto.setFilePathList(filePath);
                    emailService.sendAttachmentsMail(sendEmailDto);

                    // 添加更新内容
                    operations.updateOne(query(where("id").is(projectDeliver.getId())), update);
                }

                // 批量更新项目投递状态为成功
                operations.execute();
            }
            logger.info("项目投递定时任务结束，共投递邮件数：" + total);
        } catch (Exception e) {
            logger.error("项目投递定时任务异常：" + e);
        }
    }
}
