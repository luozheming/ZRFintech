package com.enums;

public enum CommentType {
    QUESTIONSANDANSWERS(1, "投资人线上问答"),
    COMMUNICATEBYONLINE(2, "投资人1V1交流(线上)"),
    COMMUNICATEBYOFFLINE(3, "投资人1V1交流(线下)");

    private int code;
    private String message;

    CommentType(int code, String message) {
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
        for (CommentType commentType : CommentType.values()) {
            if (code == commentType.getCode()) {
                msg = commentType.getMessage();
            }
        }
        return msg;
    }
}
