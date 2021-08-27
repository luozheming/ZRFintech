package com.dto.outdto;

import lombok.Data;

import java.util.Date;

@Data
public class AttentionOutDto {
    /**
     * 主键id
     */
    private String id;
    /**
     * 关注者的用户id
     */
    private String userId;
    /**
     * 关注者的用户角色编码
     */
    private String roleCode;
    /**
     * 关注对象的用户id
     */
    private String attentionUserId;
    /**
     * 关注对象的用户名称
     */
    private String attentionUserName;
    /**
     * 关注对象的公司名称
     */
    private String attentionCompanyName;
    /**
     * 关注对象的职位
     */
    private String attentionPositionName;
    /**
     * 关注对象的角色
     */
    private String attentionRoleCode;
    /**
     * 关注对象的头像路径
     */
    private String attentionPhotoRoute;
    /**
     * 项目编号（冗余字段，关注对象为创业者时使用）
     */
    private String projectNo;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 被关注对象是否金卡会员：true-是，false-否
     */
    private Boolean isValid;
    /**
     * 被关注对象是否已认证：true-是，false-否
     */
    private Boolean isVerify;
    /**
     * 关注对象的实体对象（如：创业者、投资人、FA个人、FA机构）
     */
    private Object attentionObject;
}
