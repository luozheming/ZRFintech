package com.dto.indto;

import lombok.Data;

import java.util.List;

@Data
public class SendEmailDto {
    /**
     * 发送方
     */
    private String sender;
    /**
     * 接收方
     */
    private String receiver;
    /**
     * 邮件主题
     */
    private String theme;
    /**
     * 邮件内容
     */
    private String content;
    /**
     * 附件list
     */
    private List<String> filePathList;
}
