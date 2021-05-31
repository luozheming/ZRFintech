package com.pojo;


import lombok.Builder;
import lombok.Data;
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
     * 开展日期 格式；YYYYMMDD
     */
    private String startDate;
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
     */
    private Integer projectStatus;
    /**
     * 参与者微信号
     */
    private String weChatNo;
    /**
     * 活动类型：0-其他，1-路演活动，2-沙龙
     */
    private Integer activityType;

}
