package com.dto.indto.contract;

import lombok.Data;

@Data
public class GetCompanyVerifyUrlDto {
    /**
     * 客户编号:注册账号时返回
     */
    private String customer_id;
    /**
     * 实名认证套餐类型
     */
    private String verifyed_way;
    /**
     * 是否允许用户页面修改：1允许 2不允许
     */
    private String page_modify;
    /**
     * 企业负责人身份:1. 法人 2. 代理人
     */
    private String company_principal_type;
    /**
     * 回调地址
     */
    private String return_url;
    /**
     * 同步通知url
     */
    private String notify_url;
}
