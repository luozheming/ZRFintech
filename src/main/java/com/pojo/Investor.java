package com.pojo;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

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
    private String pthoneNm;
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
     * 行业标签1
     */
    private String indusLab1;
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

    @Tolerate
    public Investor() {}
}
