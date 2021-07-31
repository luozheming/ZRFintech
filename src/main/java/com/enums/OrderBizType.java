package com.enums;

/**
 * 订单业务类型
 */
public enum OrderBizType {
    PURCHASEVIP(0, "购买会员"),
    PROJECTCOMMENT(1, "对接机构投资人-在线问答"),
    ONLINECONVERSATION(2, "对接机构投资人-线上1v1交流"),
    OFFLINECONVERSATION(3, "对接机构投资人-线下1v1交流"),
    BPOPTIMIZE(4, "商业计划书优化"),
    BPDELIVER(5, "商业计划书代投"),
    STANDARDROADSHOWCOACH(6, "标准版路演辅导"),
    DELUXEROADSHOWCOACH(7, "高级版路演辅导");

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
