package com.pojo;


import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection = "activityRecord")
public class ActivityRecord {
    /**
     * 主键id
     */
    private String id;
    /**
     * 活动id
     */
    private String activityId;
    /**
     * 主题
     */
    private String theme;
    /**
     * 活动起始日期
     */
    private String startDate;
    /**
     * 活动截止日期
     */
    private String endDate;
    /**
     * 开展日期:由起始日期拼接，具体样式由前端控制
     */
    private String activityDate;
    /**
     * 参与者姓名
     */
    private String participantName;
    /**
     * 参与者公司名称
     */
    private String participantCompanyName;
    /**
     * 参与者联系电话
     */
    private String participantPhoneNm;
    /**
     * 创建日期
     */
    private Date createTime;
    /**
     * 项目编号
     */
    private String projectNo;
    /**
     * 项目名称
     */
    private String projectNm;
    /**
     * 状态（路演项目）：1-审核中，2-等待审核结果，3-审核通过，4-暂未通过
     * 由projectStatus改为activityStatus
     */
    private Integer dealStatus;
    /**
     * 状态：1-进行中，2-已结束
     */
    private Integer activityStatus;
    /**
     * 参与者微信号
     */
    private String weChatNo;
    /**
     * 活动类型：1-路演活动，2-沙龙，3-创业大赛
     */
    private Integer activityType;
    /**
     * 活动开展方式：1-线上，2-线下
     */
    private Integer activityMode;
    /**
     * 用户id
     */
    private String userId;

    @Tolerate
    public ActivityRecord() {}
}
