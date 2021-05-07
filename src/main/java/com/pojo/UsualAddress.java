package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Builder
@Document(value = "usualAddress")
public class UsualAddress {
    /**
     * 主键ID
     */
    private String id;
    /**
     * 用户ID
     */
    private String userId;
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
    public UsualAddress(){};

}
