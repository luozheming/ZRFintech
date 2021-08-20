package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection = "browseHistory")
public class BrowseHistory {
    /**
     * 主键id
     */
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 项目编号
     */
    private String projectNo;
    /**
     * 项目名称
     */
    private String projectNm;
    /**
     * 所属行业
     */
    private String proIndus;
    /**
     * 融资轮次
     */
    private String finRound;
    /**
     * 企业所在城市
     */
    private String entCity;
    /**
     * 项目简介；1句话描述
     */
    private String proBriefDes;
    /**
     * 累计浏览次数
     */
    private Integer browseTimes;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    @Tolerate
    public BrowseHistory() {}
}
