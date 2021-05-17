package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "carouselPicture")
public class CarouselPicture {
    /**
     * 主键id
     */
    private String id;
    /**
     * 跳转链接
     */
    private String linkUrl;
    /**
     * 图片数据
     */
    private String photo;
    /**
     * 图片路径
     */
    private String photoRoute;
    /**
     * 1-小程序，2-PC
     */
    private Integer photoType;
    /**
     * 排列序号
     */
    private Integer orderNo;
    /**
     * 状态：0-启用，1-禁用
     */
    private Integer status;
    @Tolerate
    public CarouselPicture(){}
}
