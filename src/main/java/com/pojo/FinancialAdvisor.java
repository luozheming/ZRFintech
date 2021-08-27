package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Builder
@Document(collection = "financialAdvisor")
public class FinancialAdvisor {
    /**
     * 主键faId（同user表的userId）
     */
    private String faId;
    /**
     * 个人FA的所属机构faId
     */
    private String orgFaId;
    /**
     * fa类型：1-fa个人,2-fa机构
     */
    private Integer faType;
    /**
     * 机构名称
     */
    private String orgNm;
    /**
     * 职位
     */
    private String positionName;
    /**
     * 手机号码
     */
    private String phoneNm;
    /**
     * FA姓名
     */
    private String faName;
    /**
     * 机构简介
     */
    private String introd;
    /**
     * 个人简介
     */
    private String selfIntroduction;
    /**
     * 联系人
     */
    private String contactUser;
    /**
     * 联系电话
     */
    private String telephoneNo;
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
    private String focusFiled;
    /**
     * 关注轮次
     */
    private String finRound;
    /**
     * 关注地区
     */
    private String city;
    /**
     * 投资案例
     */
    private String investmentCase;
    /**
     * 状态：0-有效，1-失效
     */
    private Integer status;
    /**
     * 是否展示：0-不展示，1-FA授权展示
     */
    private Integer showFlag;
    /**
     * 数据来源
     */
    private String sourceDesc;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 角色编码
     */
    private String roleCode;
    /**
     * 是否被关注
     */
    private Boolean isAttention;

    @Tolerate
    public FinancialAdvisor() {}
}
