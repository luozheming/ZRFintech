package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Builder
@Document(value = "vipCardUsageLog")
public class VIPCardUsageLog {
    /**
     * 主键id
     */
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 客户会员卡id
     */
    private String vipCardUsageId;
    /**
     * vip服务类型：1-项目群发
     */
    private Integer vipServiceType;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 使用的次数
     */
    private Integer usageTimes;
    /**
     * 投递目标主键id
     */
    private List<String> investorIdList;

    @Tolerate
    public VIPCardUsageLog() {}
}
