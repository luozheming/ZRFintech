package com.service;

import com.dto.indto.SendEmailDto;

public interface EmailService {
    void sendSimpleTextMail(SendEmailDto sendEmailDto)  throws Exception ;
    void sendAttachmentsMail(SendEmailDto sendEmailDto)  throws Exception ;
}
