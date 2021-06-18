package com.pojo.contract;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection = "contractCustomer")
public class ContractCustomer {
    /**
     * 外部电子合同平台注册返回的客户id
     */
    private String customerId;
    /**
     * 系统内部的用户id
     */
    private String userId;
    /**
     * 交易号（需要保存，用于证书申请和实名认证查询）
     */
    private String transactionNo;
    /**
     * 认证请求地址（需要保存，遇到中途退出认证或页面过期等情况可重新访问）
     */
    private String url;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    @Tolerate
    public ContractCustomer(){};
}
