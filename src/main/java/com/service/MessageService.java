package com.service;

import com.pojo.Message;

import java.util.List;

public interface MessageService {
    List<Message> pageList(String userId, Integer pageNum, Integer pageSize);
    Integer count(String userId);
    void deleteMessage(String userId, String id);
    void readMessage(String userId);
    void add(Message message);
}
