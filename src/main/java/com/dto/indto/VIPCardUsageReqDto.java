package com.dto.indto;

import lombok.Data;

import java.util.Date;

@Data
public class VIPCardUsageReqDto {
    /**
     * 主键id
     */
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户的openId(冗余字段)
     */
    private String openId;
    /**
     * vip卡片id
     */
    private String vipCardId;
    /**
     * vip卡类型：1-月卡，2-季卡，3-年卡
     */
    private Integer vipCardType;
    /**
     * 会员卡开始时间
     */
    private Date startTime;
    /**
     * 会员卡结束时间
     */
    private Date endTime;
}
