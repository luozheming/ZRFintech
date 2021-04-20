package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@Document(value = "projectBpApply")
public class ProjectBpApply {
    /**
     * 主键ID
     */
    private String id;
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
     * 是否注册：true-是,false-否
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
     * 股份出让比例
     */
    private BigDecimal sharesTransfer;
    /**
     * 项目联系人
     */
    private String proUser;
    /**
     * 项目联系人电话
     */
    private String proPhonum;
    /**
     * 上传用户
     */
    private String openId;
    /**
     * 手机号码
     */
    private String phoneNm;
    /**
     * 申请时间
     */
    private Date createTime;
    /**
     * 完成时间
     */
    private Date updateTime;
    /**
     * 状态:0-未处理，1-已处理，2-不予处理
     */
    private Integer status;

    @Tolerate
    public ProjectBpApply() {}
}
