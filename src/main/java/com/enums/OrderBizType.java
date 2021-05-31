package com.enums;

/**
 * 订单业务类型
 */
public enum OrderBizType {
    PROJECTCOMMENT(1, "评论付费"),
    ACTIVITYRECORD(2, "活动付费");

    private int code;
    private String message;

    OrderBizType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return message;
    }

    public static String getMessage(int code) {
        String msg = "";
        for (OrderBizType commentType : OrderBizType.values()) {
            if (code == commentType.getCode()) {
                msg = commentType.getMessage();
            }
        }
        return msg;
    }

    public static OrderBizType getInstance(int code) {
        for (OrderBizType commentType : OrderBizType.values()) {
            if (code == commentType.getCode()) {
                return commentType;
            }
        }
        return null;
    }
}
