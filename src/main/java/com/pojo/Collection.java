package com.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Collection {
    /**
     * 主键id
     */
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户角色编码
     */
    private String roleCode;
    /**
     * 项目编号
     */
    private String projectNo;
    /**
     * 项目名称
     */
    private String projectNm;
    /**
     * 所属行业
     */
    private String proIndus;
    /**
     * 融资轮次
     */
    private String finRound;
    /**
     * 企业所在城市
     */
    private String entCity;
    /**
     * 项目简介；1句话描述
     */
    private String proBriefDes;
    /**
     * bp路径
     */
    private String bpRoute;
    /**
     * logo路径
     */
    private String logoRoute;
    /**
     * 创建时间
     */
    private Date createTime;
}
