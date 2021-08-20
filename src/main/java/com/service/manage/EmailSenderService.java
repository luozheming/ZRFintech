package com.service.manage;

import com.pojo.EmailSender;

import java.util.List;

public interface EmailSenderService {
    EmailSender detail(String id);
    List<EmailSender> list();
    void update(EmailSender EmailSender);
    void insert(EmailSender EmailSender);
    void status(EmailSender EmailSender);
}
