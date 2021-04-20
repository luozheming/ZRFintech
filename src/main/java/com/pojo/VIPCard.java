package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@Document(value = "vipCard")
public class VIPCard {
    /**
     * 主键id
     */
    private String id;
    /**
     * 原价
     */
    private BigDecimal price;
    /**
     * 折扣价
     */
    private BigDecimal discountPrice;
    /**
     * 商业计划书定制服务次数
     */
    private Integer bpApplyTimes;
    /**
     * 商业计划书专业评审服务
     */
    private Integer commentTimes;
    /**
     * 其他不限次数的增值服务
     */
    private List<String> additionalService;

    @Tolerate
    public VIPCard() {}
}
