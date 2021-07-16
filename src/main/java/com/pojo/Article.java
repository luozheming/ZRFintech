package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection = "article")
public class Article {
    /**
     * 主键id
     */
    private String id;
    /**
     * 文章标题
     */
    private String theme;
    /**
     * 文章类型:1-创业干货
     */
    private Integer articleType;
    /**
     * 图片路径
     */
    private String photoRoute;
    /**
     * 图片数据
     */
    private String photo;
    /**
     * 文章内容
     */
    private String articleContent;
    /**
     * 保存草稿标识
     */
    private Boolean isDone;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    @Tolerate
    public Article() {}
}
