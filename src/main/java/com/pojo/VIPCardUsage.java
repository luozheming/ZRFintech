package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Document(value = "vipCardUsage")
public class VIPCardUsage {
    /**
     * 主键id
     */
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户的openId
     */
    private String openId;
    /**
     * 金卡卡种id
     */
    private String vipCardId;
    /**
     * 会员卡开始时间
     */
    private Date startTime;
    /**
     * 会员卡结束时间
     */
    private Date endTime;
    /**
     * 是否金卡模板：true-是，false-否
     */
    private Boolean isVipTemplate;

    @Tolerate
    public VIPCardUsage() {}
}
