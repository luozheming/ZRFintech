package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(value = "vipCardUsage")
public class VIPCardUsage {
    /**
     * 主键id
     */
    private String id;
    /**
     * 用户的openId
     */
    private String openId;
    /**
     * vip卡片id
     */
    private String cardId;
    /**
     * 商业计划书定制服务剩余次数
     */
    private Integer bpApplyTimes;
    /**
     * 商业计划书专业评审服务剩余次数
     */
    private Integer commentTimes;
    /**
     * 购买的卡张数
     */
    private Integer cardCount;

    @Tolerate
    public VIPCardUsage() {}
}
