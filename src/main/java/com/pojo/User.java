package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(value = "user")
public class User {
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
    private String cardRoute;
    private String cardBackRoute;

    @Tolerate
    public User() {}
}
