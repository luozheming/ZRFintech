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

}
