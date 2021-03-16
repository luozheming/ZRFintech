package com.pojo;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "entuser")
public class EntUser {
    /**
     *用户唯一识别键
     */
    @Id
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
     * 项目
     */
    private List<Project> projects;

    @Data
    public class Project {
        private String projectNo;
        private String projectNm;
    }
}