package com.utils;

import com.alibaba.fastjson.JSONObject;

public enum ErrorCode {
    SUCCESS(200, "成功"),
    USERFIRSTLOGIN(200,"用户首次登陆"),
    NULLOBJECT(601,"对象为空"),
    EMPITYFILE(602,"文件为空"),
    FILEUPLOADFAILED(604,"文件上传失败"),
    PAGEBELLOWZERO(605,"分页参数错误"),
    NULLSTARS(606,"评分为空"),
    FORBIDREPLY(607,"项目未点评不能回评"),
    CONTENTLESS(608,"评论字数不得少于200字"),
    CONTENTEMPTY(609,"项目补充信息不能为空"),
    NULLPARAM(610,"参数为空"),
    EMPITYPHONE(611,"手机号或者密码为空"),
    EMPITYUSER(612,"用户不存在"),
    PAYMENTEXCEPTION(613,"支付异常"),
    VIPNOTPAYMENT(614,"未购买VIP服务"),
    VIPNOTENOUGH(615,"VIP服务额度不足"),
    ONECECARDALREADY(616,"已经免费领取一次卡片"),
    REPEATCOMMIT(617,"重复提交"),
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
