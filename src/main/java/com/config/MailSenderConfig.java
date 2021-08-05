/*
package com.config;

import com.sun.tracing.dtrace.ArgsAttributes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
//@AllArgsConstructor
public class MailSenderConfig {

    @Autowired
    private MailConfig mailConfig;

    @Autowired
    private List<JavaMailSenderImpl> senderList;

//    private final List<JavaMailSenderImpl> senderList;

    */
/**
     * 初始化 sender
     *//*

    @PostConstruct
    public void buildMailSender(){
        List<MailConfig.MailProperties> mailConfigs = mailConfig.getConfigs();
        log.info("初始化mailSender");
        JavaMailSenderImpl javaMailSender = null;
        if (!CollectionUtils.isEmpty(mailConfigs)) {
            for (MailConfig.MailProperties mailProperties : mailConfigs) {
                javaMailSender = new JavaMailSenderImpl();
                javaMailSender.setDefaultEncoding(mailProperties.getDefaultEncoding());
                javaMailSender.setHost(mailProperties.getHost());
                javaMailSender.setPort(mailProperties.getPort());
                javaMailSender.setProtocol(mailProperties.getProtocol());
                javaMailSender.setUsername(mailProperties.getUsername());
                javaMailSender.setPassword(mailProperties.getPassword());
                // 添加数据
                senderList.add(javaMailSender);
            }
        }
    }

    */
/**
     * 获取MailSender
     * @return CustomMailSender
     *//*

    public JavaMailSenderImpl getSender(){
        if(senderList.isEmpty()){
            buildMailSender();
        }
        // 随机返回一个JavaMailSender
        return senderList.get(new Random().nextInt(senderList.size()));
    }

    */
/**
     * 清理 sender
     *//*

    public void clear(){
        senderList.clear();
    }

}
*/
