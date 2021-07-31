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
     * 项目联系人电话
     */
    private String proPhonum;
    /**
     * 上传用户
     */
    private String openId;
    /**
     * 申请时间
     */
    private Date createTime;
    /**
     * 完成时间
     */
    private Date updateTime;
    /**
     * 处理状态:0-未处理，1-已处理，2-不予处理
     */
    private Integer dealStatus;
    /**
     * 项目申请类型：4-bp优化，5-bp代投，6-路演辅导
     */
    private Integer applyType;

    @Tolerate
    public ProjectBpApply() {}
}
