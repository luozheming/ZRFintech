package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Builder
@Document(value = "integralGoodsOrder")
public class IntegralGoodsOrder {
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
    /**
     * 用户常用地址ID
     */
    private String usualAddressId;
    /**
     * 地理位置
     */
    private String location;
    /**
     * 详细地址
     */
    private BigDecimal detailAddress;
    /**
     * 邮政编码
     */
    private String zipCode;
    /**
     * 收货人姓名
     */
    private String receiverName;
    /**
     * 收货人手机号码
     */
    private String receiverPhoneNm;
    /**
     * 是否默认地址
     */
    private boolean isDefault;

    @Tolerate
    public IntegralGoodsOrder(){};

}
