package com.dto.outdto;

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
public class VIPCardUsageRespDto {
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
     * vip卡片id
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
     * 当月项目剩余投递次数
     */
    private Integer ProDeliverTimesPerMonth;
    /**
     * 当天项目剩余投递次数（免费，不计入会员卡投递次数）
     */
    private Integer ProDeliverTimesPerDay;
    /**
     * 是否有效：false-否，true-是
     */
    private Boolean isValid;

    @Tolerate
    public VIPCardUsageRespDto() {}
}
