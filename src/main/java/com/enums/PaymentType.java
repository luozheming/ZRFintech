package com.enums;

public enum PaymentType {
    ONLINEPAY(1, "线上支付"),
    OFFLINEPAY(2, "线下支付");

    private int code;
    private String message;

    PaymentType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return message;
    }

    public static String getMessage(Integer code) {
        String msg = "";
        for (PaymentType commentType : PaymentType.values()) {
            if (code == commentType.getCode()) {
                msg = commentType.getMessage();
            }
        }
        return msg;
    }
}
