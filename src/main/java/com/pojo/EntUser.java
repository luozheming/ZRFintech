package com.pojo;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Builder
@Document(collection = "entuser")
public class EntUser {
    /**
     *用户唯一识别键
     */
    private String openId;
    /**
     *手机号码
     */
    private String phoneNm;
    /**
     *用户微信昵称
     */
    private String nickName;
    /**
     * 性别
     */
    private String gender;
    /**
     * 城市
     */
    private String city;
    /**
     * 省
     */
    private String province;
    /**
     * 国家
     */
    private String country;

    /**
     * 是否需要联系客服：true-是，false-否
     */
    private Boolean isContactService;
    /**
     * 是否浏览上传项目页面
     */
    private Boolean isBrowse;
    /**
     * 是否浏览路演上传项目页面
     */
    private Boolean isShowBrowse;
    /**
     * 创建时间
     */
    private Date createTime;
}
