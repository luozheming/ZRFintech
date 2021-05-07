package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(value = "investorIntegral")
public class InvestorIntegral {
    /**
     * 主键ID
     */
    private String id;
    /**
     * 投资人 Id
     */
    private String investorId;
    /**
     * 投资人姓名
     */
    private String investorName;
    /**
     * 总积分
     */
    private String total;
    /**
     * 累计已使用积分
     */
    private String accumulateUsed;

    @Tolerate
    public InvestorIntegral() {}
}
