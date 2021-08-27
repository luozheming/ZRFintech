package com.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class City {
    /**
     * 主键id
     */
    private String id;
    /**
     * 热门城市，以逗号分隔
     */
    private String hotCity;
    /**
     * 其他城市(去除热门城市)，以逗号分隔
     */
    private String city;
    /**
     * 城市类型：1-（投资人/FA的）关注城市
     */
    private Integer cityType;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
