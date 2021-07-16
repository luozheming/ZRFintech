package com.dto.indto;

import lombok.Data;

@Data
public class ProjectBpApplyDto {
    /**
     * 项目名称
     */
    private String projectNm;
    /**
     * 所属行业
     */
    private String proIndus;
    /**
     * 企业地址
     */
    private String address;
    /**
     * 项目描述
     */
    private String proDes;
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
     * 微信号
     */
    private String weChatNo;
    /**
     * 项目申请类型：3-bp定制申请，4-FA全流程定制
     */
    private Integer applyType;
    /**
     * 客户编号
     */
    private String entUserId;
}
