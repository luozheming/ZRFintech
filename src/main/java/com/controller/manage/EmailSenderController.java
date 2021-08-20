package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.pojo.EmailSender;
import com.service.manage.EmailSenderService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emailSender")
public class EmailSenderController {

    @Autowired
    private EmailSenderService emailSenderService;

    /**
     * 获取邮箱发件人列表
     * @return
     */
    @GetMapping("/list")
    public String list() {
        try {
            List<EmailSender> emailSenders = emailSenderService.list();
            OutputFormate outputFormate = new OutputFormate(emailSenders);
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 修改发件人信息
     * @param emailSender
     * @return
     */
    @PostMapping("/edit")
    public String edit(@RequestBody EmailSender emailSender) {
        emailSenderService.update(emailSender);
        return ErrorCode.SUCCESS.toJsonString();
    }

    /**
     * 新增发件人
     * @param emailSender
     * @return
     */
    @PostMapping("/add")
    public String add(@RequestBody EmailSender emailSender) {
        emailSenderService.insert(emailSender);
        return ErrorCode.SUCCESS.toJsonString();
    }

    /**
     * 启用/禁用
     * @param emailSender
     * @return
     */
    @PostMapping("/status")
    public String status(@RequestBody EmailSender emailSender) {
        emailSenderService.status(emailSender);
        return ErrorCode.SUCCESS.toJsonString();
    }
}
