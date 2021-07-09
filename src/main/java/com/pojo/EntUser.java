package com.pojo;


import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Builder
@Document(collection = "entuser")
public class EntUser {
    /**
     * 客户id（同user表的userId）
     */
    private String entUserId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 登录密码
     */
//    private String password;
    /**
     *用户唯一识别键
     */
    private String openId;
    /**
     * 投资人 Id
     */
//    private String investorId;
    /**
     * 角色编码：ent-商户，investor-投资人
     */
//    private String roleCode;
    /**
     *手机号码
     */
    private String phoneNm;
    /**
     *用户微信昵称
     */
    private String nickName;
    /**
     *用户微信号
     */
    private String weChatNm;
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
     * 创建日期
     */
    private Date createTime;
    /**
     * 更新日期
     */
    private Date updateTime;
    /**
     * 头像数据
     */
    private String photo;
    /**
     * 头像路径
     */
    private String photoRoute;

    @Tolerate
    public EntUser() {}
    /**
     * 是否浏览上传项目页面
     */
    private Boolean isBrowse;
    /**
     * 是否浏览路演上传项目页面
     */
    private Boolean isShowBrowse;
}
