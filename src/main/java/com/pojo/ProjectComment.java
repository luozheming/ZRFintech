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
     * 企业用户ID
     */
    private String entUserId;
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
     * 追问附件路径
     */
    private String questionFilePath;
    /**
     * 评论附件路径
     */
    private String commentFilePath;
    /**
     * 评论类型：1-点评，2-追问
     */
    private Integer commentType;
    /**
     * 追问问题
     */
    private String question;

    @Tolerate
    public ProjectComment() {}
}
