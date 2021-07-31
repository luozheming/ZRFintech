package com.enums;

public enum RoleCode {
    MANAGEMENT("management", "管理员"),
    ENTUSER("entuser", "企业客户"),
    INVESTOR("investor", "投资人"),
    FINANCIALADVISOR("financialAdvisor", "FA机构"),
    VISITOR("visitor", "游客");

    private String code;
    private String message;

    RoleCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return message;
    }

    public static String getMessage(String code) {
        String msg = "";
        for (RoleCode roleCode : RoleCode.values()) {
            if (code.equals(roleCode.getCode())) {
                msg = roleCode.getMessage();
            }
        }
        return msg;
    }
}
