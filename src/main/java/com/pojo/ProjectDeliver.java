package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
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
     * 项目名称
     */
    private String projectNm;
    /**
     * bp文件路径
     */
    private String bpRoute;
    /**
     * 所属行业
     */
    private String proIndus;
    /**
     * 融资轮次
     */
    private String finRound;
    /**
     * 期望融资额度
     */
    private String quota;
    /**
     * 股份出让比例
     */
    private BigDecimal sharesTransfer;
    /**
     * 项目联系人
     */
    private String proUser;
    /**
     * 项目联系人电话
     */
    private String proPhonum;
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
     * 投递状态：0-录入成功未投递，1-定时投递成功，2-失败
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
