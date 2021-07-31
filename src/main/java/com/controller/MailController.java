package com.controller;

import com.service.EmailService;
import com.utils.ErrorCode;
import com.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mail")
public class MailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/sendSimpleTextMail")
    public String sendSimpleTextMail() {
        try {
            emailService.sendSimpleTextMail();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        return ErrorCode.SUCCESS.toJsonString();
    }

    @GetMapping("/sendAttachmentsMail")
    public String sendAttachmentsMail() {
        try {
//            emailService.sendAttachmentsMail(sendEmailDto);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        return ErrorCode.SUCCESS.toJsonString();
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10000; i++) {
            HttpClientUtil.doGet("http://127.0.0.1:8888/mail/sendAttachmentsMail");
            Thread.sleep(60*1000);
            if ((i > 50) && (i % 50 == 0)) {
                Thread.sleep(10*60*1000);
            }
            System.out.println("i=" + i);
        }
    }
}
