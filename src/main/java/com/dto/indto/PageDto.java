package com.dto.indto;

import lombok.Data;

@Data
public class PageDto {
    /**
     * 页数
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 所属行业
     */
    private String proIndus;
    /**
     * 融资轮次
     */
    private String finRound;
    /**
     * 所在城市
     */
    private String city;
    /**
     * 是否自动匹配
     */
    private Boolean isAutoMatch;
    /**
     * 角色编码
     */
    private String roleCode;
    /**
     * fa类型：1-fa个人,2-fa机构
     */
    private Integer faType;
    /**
     * 个人FA的所属机构faId
     */
    private String orgFaId;
    /**
     * 是否展示：0-否，1-展示
     */
    private Integer showFlag;

}
