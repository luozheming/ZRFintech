package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Document(value = "vipCard")
public class VIPCard {
    /**
     * 主键id
     */
    private String id;
    /**
     * 卡种为金卡的卡片id
     */
    private String cardId;
    /**
     * 原价
     */
    private BigDecimal price;
    /**
     * 折扣价
     */
    private BigDecimal discountPrice;
    /**
     * vip卡类型：1-月卡，2-季卡，3-年卡
     */
    private Integer vipCardType;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 排列顺序
     */
    private Integer orderNo;

    /**
     *  卡片服务内容
     */
    private List<Map<String, Object>> vipService;

    @Tolerate
    public VIPCard() {}
}
