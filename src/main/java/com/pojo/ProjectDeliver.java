package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(value = "projectDeliver")
public class ProjectDeliver {
    /**
     * 主键id
     */
    private String id;
    /**
     * vip卡使用记录id（为空时表示单个bp投递，不计入会员使用次数）
     */
    private String vipCardUsageLogId;
    /**
     *  用户id
     */
    private String userId;
    /**
     *  项目编号
     */
    private String projectNo;
    /**
     * bp文件路径
     */
    private String bpRoute;
    /**
     *  项目投递目标：1-VC,2-FA
     */
    private Integer targetType;
    /**
     * 投递目标对象
     */
    private Object targetObject;
    /**
     * 投递目标邮箱
     */
    private String targetEmail;
    /**
     * 投递状态：0-未投递，1-成功，2-失败
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 更新时间
     */
    private Date updateDate;

    @Tolerate
    public ProjectDeliver() {}
}
