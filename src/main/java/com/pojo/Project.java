package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Document(value = "project")
public class Project {
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
     * 是否注册公司：false-否，true-是
     */
    private Boolean isRegister;
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
     * 用户手机号
     */
    private String phoneNm;
    /**
     *  BP路径
     */
    private String bpRoute;
    /**
     * 期望点评列表
     */
    List<String> expList;
    /**
     * 项目联系人
     */
    private String proUser;
    /**
     * 项目联系人电话
     */
    private String proPhonum;
    /**
     * 项目联系人邮箱
     */
    private String proEmail;
    /**
     * 存草稿标识位
     */
    private Boolean isDone;
    /**
     * 付费标识
     */
    private Boolean isPay;
    /**
     * 股份出让比例
     */
    private BigDecimal sharesTransfer;
    /**
     * 项目评论信息
     */
    private List<ProjectComment> projectCommentList;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    @Tolerate
    public Project() {}
}
