package com.dto.indto.contract;

import lombok.Data;

@Data
public class AccountRegisterDto {
    /**
     * 用户在接入方的唯一标识
     */
    private String open_id;
    /**
     * 账户类型：1:个人  2:企业
     */
    private String account_type;
}
