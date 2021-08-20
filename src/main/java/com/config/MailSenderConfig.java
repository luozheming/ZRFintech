package com.config;

import com.pojo.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Slf4j
@Component
public class MailSenderConfig {

    @Autowired
    private MongoTemplate mongoTemplate;

    private final List<JavaMailSenderImpl> senderList;

    public MailSenderConfig(List<JavaMailSenderImpl> senderList) {
        this.senderList = senderList;
    }

    /**
     * 初始化 sender
     * */
    @PostConstruct
    public void buildMailSender(){
        List<EmailSender> emailSenders = mongoTemplate.find(query(where("status").is(0)), EmailSender.class);
        log.info("初始化mailSender");
        JavaMailSenderImpl javaMailSender = null;
        if (!CollectionUtils.isEmpty(emailSenders)) {
            for (EmailSender emailSender : emailSenders) {
                javaMailSender = new JavaMailSenderImpl();
                javaMailSender.setDefaultEncoding(emailSender.getDefaultEncoding());
                javaMailSender.setHost(emailSender.getHost());
                javaMailSender.setPort(emailSender.getPort());
                javaMailSender.setProtocol(emailSender.getProtocol());
                javaMailSender.setUsername(emailSender.getUserName());
                javaMailSender.setPassword(emailSender.getPassword());
                Properties properties = new Properties();
                properties.setProperty("mail.smtp.auth", "true");
                properties.setProperty("mail.smtp.socketFactory.port", "465");
                properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                javaMailSender.setJavaMailProperties(properties);
                // 添加数据
                senderList.add(javaMailSender);
            }
        }
    }

    /**
     * 获取MailSender
     * @return CustomMailSender
     * */
    public JavaMailSenderImpl getSender(){
        if(senderList.isEmpty()){
            buildMailSender();
        }
        // 随机返回一个JavaMailSender
        return senderList.get(new Random().nextInt(senderList.size()));
    }

    /**
     * 清理 sender
     * */
    public void clear(){
        senderList.clear();
    }

}
