package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(value = "message")
public class Message {
    /**
     * 主键ID
     */
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 发送方
     */
    private String sender;
    /**
     * 消息内容
     */
    private String msg;
    /**
     * 创建时间
     */
    private Date createTime;

    @Tolerate
    public Message(){};
}
