package com.pojo;

import java.util.Date;
import java.util.List;

public class FinancialAdvisor {
    /**
     * 主键faId（同user表的userId）
     */
    private String faId;
    /**
     * 机构名称
     */
    private String orgNm;
    /**
     * 手机号码
     */
    private String phoneNm;
    /**
     * FA姓名
     */
    private String faName;
    /**
     * 个人简介
     */
    private String introd;
    /**
     * 邮箱
     */
    private String email;
    /**
     * FA照片路径
     */
    private String  photoRoute;
    /**
     * 关注领域
     */
    private List<String> focusFiled;
    /**
     * 关注轮次
     */
    private List<String> finRound;
    /**
     * 关注地区
     */
    private List<String> area;
    /**
     * 投资案例
     */
    private List<String> investmentCase;
    /**
     * 创建时间
     */
    private Date createTime;
}
