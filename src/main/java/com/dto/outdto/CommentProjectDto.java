package com.dto.outdto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CommentProjectDto {
    /**
     * 评论ID
     */
    private String id;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 重点关注:1-感兴趣，2-未标记，3-不感兴趣，4-拒绝
     */
    private Integer favor;
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
     * 期望融资额度
     */
    private String quota;
    /**
     * 团队人数
     */
    private String teamSize;
    /**
     * 企业营收状况
     */
    private String finSt;
    /**
     * 企业所在省
     */
    private String entProvince;
    /**
     * 企业所在城市
     */
    private String entCity;
    /**
     * 项目描述
     */
    private String proDes;
    /**
     * 上传用户
     */
    private String openId;
    /**
     *  BP路径
     */
    private String bpRoute;
    /**
     * 项目补充信息
     */
    private String proCompl;
    /**
     * 项目联系人
     */
    private String proUser;
    /**
     * 项目联系人电话
     */
    private String proPhonum;
}
