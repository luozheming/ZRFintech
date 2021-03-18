package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

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
     * 项目编号
     */
    private String projectNo;
    /**
     * 项目名称
     */
    private String projectNm;
    /**
     * 上传用户
     */
    private String openId;
    /**
     * 重点关注
     */
    private Boolean favor;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论时间
     */
    private Date updateTm;
    /**
     * 评论星级
     */
    private Integer stars;
    /**
     * 用户回评
     */
    private String reply;
    /**
     * 回复时间
     */
    private Date replyTm;

    @Tolerate
    public ProjectComment() {}
}
