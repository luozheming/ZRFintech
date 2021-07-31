package com.service.impl;

import com.dto.indto.SendEmailDto;
import com.service.EmailService;
import com.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private CommonUtils commonUtils;
    @Value("${s3BucketName}")
    private String s3BucketName;
    @Value("${localTempFilePath}")
    private String localTempFilePath;

    /**
     * 发送简单文本邮件
     */
    @Override
    public void sendSimpleTextMail() throws Exception {
        // 设置邮件发送内容
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        // 发件人: setFrom处必须填写自己的邮箱地址，否则会报553错误
//        mailMessage.setFrom("18923870661@163.com");
        mailMessage.setFrom("zr20210728@163.com");
        // 收件人
        mailMessage.setTo("454615783@qq.com");
//        mailMessage.setCc("18923870661@163.com");
        // 主题
        mailMessage.setSubject("商业计划书投递");
        // 内容
        mailMessage.setText("附件为商业计划书，感兴趣可以联系。");
        try {
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            System.out.println("-----发送简单文本邮件失败!-------" + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void sendAttachmentsMail(SendEmailDto sendEmailDto) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(sendEmailDto.getSender());
        helper.setTo(sendEmailDto.getReceiver());
        helper.setSubject("邮件主题");
        helper.setText("邮件内容");

        String fileName = "";
        File file = null;
        if (!CollectionUtils.isEmpty(sendEmailDto.getFilePathList())) {
            for (String filePath : sendEmailDto.getFilePathList()) {
                fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
                // 获取文件信息
                file = commonUtils.downloadS3File(s3BucketName, filePath, localTempFilePath + fileName);
//                file = commonUtils.downloadS3File(s3BucketName, filePath, "d:/" + tempFilePath.substring(0, tempFilePath.lastIndexOf("/")));
                if (file.exists()) {
                    helper.addAttachment(fileName, file);
                }
            }
        }
//        FileSystemResource file = new FileSystemResource(new File("D:\\test.pdf"));
//        helper.addAttachment("test.pdf", file);

        javaMailSender.send(mimeMessage);
    }
}
