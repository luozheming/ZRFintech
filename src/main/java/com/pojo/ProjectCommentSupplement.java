package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@Document(value = "projectCommentSupplement")
public class ProjectCommentSupplement {
    /**
     * 评论补充追问主键
     */
    private String id;
    /**
     * 评论主键
     */
    private String projectCommentId;
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
     * 补充信息类型：0-追问，1-回复
     */
    private Integer supplementType;
    /**
     * 追问内容
     */
    private String supplement;
    /**
     * 评论时间
     */
    private Date createTime;
    /**
     * 评论资费
     */
    private BigDecimal supplementAmount;
    /**
     * 文件内容
     */
    private String file;
    /**
     * 文件路径
     */
    private String filePath;

    @Tolerate
    public ProjectCommentSupplement() {}
}
