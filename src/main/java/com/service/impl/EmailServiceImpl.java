package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.config.MailSenderConfig;
import com.dto.indto.SendEmailDto;
import com.enums.RoleCode;
import com.pojo.User;
import com.service.EmailService;
import com.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
//@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Autowired
    private MailSenderConfig senderConfig;
//    @Autowired
//    private JavaMailSender javaMailSender;
    @Autowired
    private CommonUtils commonUtils;
    @Value("${s3BucketName}")
    private String s3BucketName;
    @Value("${localTempFilePath}")
    private String localTempFilePath;

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    /**
     * 发送简单文本邮件
     */
    @Override
    public void sendSimpleTextMail(SendEmailDto sendEmailDto) throws Exception {
        JavaMailSenderImpl javaMailSender = senderConfig.getSender();

        logger.info("邮件发送者：" + javaMailSender.getUsername());

        // 设置邮件发送内容
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        // 发件人: setFrom处必须填写自己的邮箱地址，否则会报553错误
        mailMessage.setFrom(javaMailSender.getUsername());
        // 收件人
        mailMessage.setTo(sendEmailDto.getReceiver());
        // 主题
        mailMessage.setSubject(sendEmailDto.getTheme());
        // 内容
        mailMessage.setText(sendEmailDto.getContent());
        javaMailSender.send(mailMessage);
    }

    @Override
    public void sendAttachmentsMail(SendEmailDto sendEmailDto) throws Exception {
        JavaMailSenderImpl javaMailSender = senderConfig.getSender();
        logger.info("邮件发送者：" + javaMailSender.getUsername());

        logger.info("邮件发送入参：" + JSONObject.toJSONString(sendEmailDto));
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(sendEmailDto.getSender());
        helper.setTo(sendEmailDto.getReceiver());
        helper.setSubject(sendEmailDto.getTheme());
        helper.setText(sendEmailDto.getContent());

        String fileName = "";
        File file = null;
        if (!CollectionUtils.isEmpty(sendEmailDto.getFilePathList())) {
            for (String filePath : sendEmailDto.getFilePathList()) {
                fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
                // 获取文件信息
                String localFilePath = localTempFilePath + fileName;
                logger.info("附件信息，文件路径|临时文件路径：" + filePath + "|" + localFilePath);
                file = commonUtils.downloadS3File(s3BucketName, filePath, localFilePath);
                if (file.exists()) {
                    logger.info("邮件已添加附件...");
                    helper.addAttachment(fileName, file);
                }
            }
        }
        javaMailSender.send(mimeMessage);
    }
}
