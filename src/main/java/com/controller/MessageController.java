package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.pojo.Message;
import com.service.MessageService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/pageList")
    public String pageList(String userId, Integer pageNum, Integer pageSize) {
        if (pageNum < 0 || pageSize <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        try {
            int count = messageService.count(userId);
            int totalPage = count/pageSize;
            PageListDto pageListDto = new PageListDto<Message>();
            pageListDto.setTotal(count);
            if(pageNum <= totalPage){
                List<Message> messages =  messageService.pageList(userId, pageNum, pageSize);
                pageListDto.setList(messages);
            }
            OutputFormate outputFormate = new OutputFormate(pageListDto);
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @DeleteMapping("/delMessage")
    public String delMessage(String userId, String id) {
        messageService.deleteMessage(userId, id);
        return ErrorCode.SUCCESS.toJsonString();
    }

    /**
     * 消息标记已读
     * @param userId
     * @return
     */
    @PostMapping("/readMessage")
    public String readMessage(String userId) {
        messageService.readMessage(userId);
        return ErrorCode.SUCCESS.toJsonString();
    }

}
