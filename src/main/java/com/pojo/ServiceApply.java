package com.pojo;


import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection = "serviceApply")
public class ServiceApply {
    /**
     * 主键id
     */
    private String id;
    /**
     * 服务申请类型:1-政策补贴，2-法律咨询
     */
    private Integer serviceApplyType;
    /**
     * 服务申请名称
     */
    private String serviceApplyName;
    /**
     * 用户id
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
     * 公司
     */
    private String companyName;
    /**
     * 职称
     */
    private String positionName;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     *  联系电话号码
     */
    private String telephoneNo;
    /**
     * 0-未处理，1-已处理
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;

    @Tolerate
    public ServiceApply() {}
}
