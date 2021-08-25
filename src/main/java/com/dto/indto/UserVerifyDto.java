package com.dto.indto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(value = "user")
public class UserVerifyDto {
    /**
     * 用户ID，主键
     */
    private String userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 手机号码
     */
    private String phoneNm;
    /**
     * 登录密码
     */
    private String password;
    /**
     * 角色编码：entuser-商户，investor-投资人，FA-融资顾问，visitor-游客，management-管理员
     */
    private String roleCode;
    /**
     * 是否注销：0-否，1-是
     */
    private Integer isDelete;
    /**
     * 创建日期
     */
    private Date createTime;
    /**
     * 更新日期
     */
    private Date updateTime;
    /**
     * 是否认证
     */
    private Boolean isVerify;
    /**
     * 头像数据
     */
    private String photo;
    /**
     * 头像路径
     */
    private String photoRoute;
    /**
     * 公司
     */
    private String companyName;
    /**
     * 职称
     */
    private String positionName;
    /**
     * 微信登录唯一标识
     */
    private String openId;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     *  联系电话号码
     */
    private String telephoneNo;
    /**
     * 名片正面路径
     */
    private String cardRoute;
    /**
     * 名片反面路径
     */
    private String cardBackRoute;
    /**
     * 审核状态：1-待审核，2-审核通过，3-审核不通过
     */
    private Integer auditStatus;
    /**
     * 认证身份申请的角色编码
     */
    private String auditRoleCode;
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
    private String focusCity;
    /**
     * 投资案例
     */
    private String investmentCase;
    /**
     * 个人简介
     */
    private String selfIntroduction;
    /**
     * 是否展示：0-不展示，1-FA授权展示
     */
    private Integer showFlag;

    @Tolerate
    public UserVerifyDto() {}
}
