package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@Document(value = "investor")
public class Investor {
    /**
     * 投资人 Id
     */
    private String investorId;
    /**
     * 机构名称
     */
    private String orgNm;
    /**
     * 手机号码
     */
    private String phoneNm;
    /**
     * 投资人姓名
     */
    private String investor;
    /**
     * 投资人介绍
     */
    private String introd;
    /**
     * 投资人照片路径
     */
    private String  invesPhotoRoute;
    /**
     * 投资人机构照片路径
     */
    private String  invesOrgPhotoRoute;
    /**
     * 投资人邮箱
     */
    private String invesEmail;
    /**
     * 行业标签list
     */
    private List<String> indusLabList;
    /**
     * 投资人头像数据
     */
    private String photo;
    /**
     * 机构图片数据
     */
    private String orgPhoto;
    /**
     *  评论资费原价
     */
    private BigDecimal price;
    /**
     * 评论资费折扣价
     */
    private BigDecimal disCountPrice;
    /**
     * 投资人未获取的资费金额
     */
    private BigDecimal unaccomplishedAmount;
    /**
     * 投资人已获取的资费金额
     */
    private BigDecimal accomplishedAmount;
    /**
     * 是否平台投资人：true-是，false-否
     */
    private Boolean isPlatform;

    @Tolerate
    public Investor() {}
}
