package com.utils;

import com.alibaba.fastjson.JSONObject;

public enum ErrorCode {
    SUCCESS(200, "success"),
    USERFIRSTLOGIN(200,"用户首次登陆"),
    NULLOBJECT(601,"对象为空"),
    OTHEREEEOR(669,"其余未知错误");

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return message;
    }

    public String toJsonString(){
        JSONObject json = new JSONObject();
        json.put("code",this.code);
        json.put("message",this.message);
        return json.toString();
    }
}
