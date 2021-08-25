package com.dto.outdto;


import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ActivityDto {
    /**
     * 主键id
     */
    private String id;
    /**
     * 主题
     */
    private String theme;
    /**
     * 副标题
     */
    private String subTheme;
    /**
     * 举办城市
     */
    private String city;
    /**
     * 图片路径
     */
    private String photoRoute;
    /**
     * 图片数据
     */
    private String photo;
    /**
     * 活动起始日期
     */
    private String startDate;
    /**
     * 活动截止日期
     */
    private String endDate;
    /**
     * 活动类型：1-路演活动，2-沙龙
     */
    private Integer activityType;
    /**
     * 活动开展方式：1-线上，2-线下
     */
    private Integer activityMode;
    /**
     * 活动内容
     */
    private String activityContent;
    /**
     * 状态：1-进行中，2-已结束
     */
    private Integer status;
    /**
     * 活动价格
     */
    private BigDecimal price;
    /**
     * 活动折扣价
     */
    private BigDecimal disCountPrice;
    /**
     * 保存草稿标识
     */
    private Boolean isDone;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 是否已报名：true-是，false-否
     */
    private Boolean isParticipant;
}
