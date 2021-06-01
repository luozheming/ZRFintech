package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@Document(value = "projectComment")
public class ProjectComment {
    /**
     * 评论主键
     */
    private String id;
    /**
     * 投资人Id
     */
    private String investorId;
    /**
     * 投资人姓名
     */
    private String investor;
    /**
     * 投资人机构名称
     */
    private String investorOrgNm;
    /**
     * 投资人照片路径
     */
    private String  invesPhotoRoute;
    /**
     * 投资人照片信息
     */
    private String  photo;
    /**
     * 项目编号
     */
    private String projectNo;
    /**
     * 项目名称
     */
    private String projectNm;
    /**
     * 项目所在地城市
     */
    private String entCity;
    /**
     * 上传用户
     */
    private String openId;
    /**
     * 重点关注:1-感兴趣，2-未标记，3-不感兴趣，4-拒绝
     */
    private Integer favor;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论时间
     */
    private Date updateTm;
    /**
     * 完成标识
     */
    private Boolean isDone;
    /**
     * 评论星级
     */
    private BigDecimal stars;
    /**
     * 用户回评
     */
    private String reply;
    /**
     * 回复时间
     */
    private Date replyTm;
    /**
     * 评论资费
     */
    private BigDecimal commentAmount;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 评论类型:1-线上问答，2-线上1V1交流，3-线下1V1交流
     */
    private Integer commentType;
    /**
     * 评论状态：0-待评论，1-已评论，2-已回评，3-已确认，4-已取消，5-已预约，6-取消预约，7-对接完成，8-对接失败
     */
    private Integer status;
    /**
     * 通用处理状态:0-未处理，1-处理中，2-已处理
     */
    private Integer commonStatus;
    /**
     * 订单支付状态
     */
    private Integer orderPayStatus;

    @Tolerate
    public ProjectComment() {}
}
