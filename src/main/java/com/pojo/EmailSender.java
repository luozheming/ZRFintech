package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection = "emailSender")
public class EmailSender {
    /**
     * 主键id
     */
    private String id;
    /**
     * 发件人邮箱
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * host
     */
    private String host;
    /**
     * 端口
     */
    private Integer port;
    /**
     * 协议
     */
    private String protocol;
    /**
     * 默认编码
     */
    private String defaultEncoding;
    /**
     * 0-有效，1-失效
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;

    @Tolerate
    public EmailSender(){}
}
