package com.dto.indto;

import lombok.Data;

@Data
public class ProjectBpApplyDto {
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
     * 项目申请类型
     */
    private Integer applyType;
    /**
     * 客户编号
     */
    private String entUserId;
}
