package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(value = "project")
public class Project {
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
     * 项目行业
     */
    private String proIndus;
    /**
     * 项目描述
     */
    private String proDes;
    /**
     * 项目价值
     */
    private String proVal;
    /**
     * 商业模式
     */
    private String bizModel;
    /**
     * 竞争优势
     */
    private String cptEdge;
    /**
     * 经营年限
     */
    private int operYear;
    /**
     * 财务状况
     */
    private String finSt;
    /**
     * 团队人数
     */
    private int teamSize;
    /**
     * 企业未来规划
     */
    private String ftrPlan;
    /**
     *  BP路径
     */
    private String bpRoute;
    /**
     * 期望点评列表
     */
    List<String> expList;

    @Tolerate
    public Project() {}
}
