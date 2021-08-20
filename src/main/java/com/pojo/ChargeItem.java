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
@Document(value = "chargeItem")
public class ChargeItem {
    /**
     * 主键id
     */
    private String id;
    /**
     * 收费项目类型：4-BP优化，5-BP代投，6-路演辅导(标准版)，7-路演辅导(高级版)，8-月卡，9-季卡，10-年卡，11-项目诊断
     */
    private Integer chargeItemType;
    /**
     * 收费项目名称
     */
    private String chargeItemName;
    /**
     * 原价
     */
    private BigDecimal price;
    /**
     * 折扣价
     */
    private BigDecimal discountPrice;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    @Tolerate
    public ChargeItem() {}
}
