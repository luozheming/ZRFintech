package com.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Attention {
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
}
