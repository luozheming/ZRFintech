package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Builder
@Document(value = "integralGoods")
public class IntegralGoods {
    /**
     * 主键ID
     */
    private String id;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 价值积分数
     */
    private BigDecimal integral;
    /**
     * 商品描述
     */
    private String goodsDesc;
    /**
     * 商品图片
     */
    private String photo;
    /**
     * 商品图片路径
     */
    private String photoRoute;

    @Tolerate
    public IntegralGoods(){};

}
