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
     * 所属行业
     */
    private String proIndus;
    /**
     * 团队人数
     */
    private int teamSize;
    /**
     * 企业营收状况
     */
    private String finSt;
    /**
     * 企业所在省
     */
    private String entProvince;
    /**
     * 企业所在城市
     */
    private String entCity;
    /**
     * 项目描述
     */
    private String proDes;
    /**
     * 上传用户
     */
    private String openId;
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
