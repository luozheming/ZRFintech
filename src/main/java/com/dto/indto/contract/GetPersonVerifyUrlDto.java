package com.dto.indto.contract;

import lombok.Data;

@Data
public class GetPersonVerifyUrlDto {
    /**
     * 客户编号:注册账号时返回
     */
    private String customer_id;
    /**
     * 实名认证套餐类型
     */
    private String verifyed_way;
    /**
     * 是否允许用户页面修改：1允许 2不允许
     */
    private String page_modify;
    /**
     * 回调地址
     */
    private String return_url;
    /**
     * 同步通知url
     */
    private String notify_url;
    /**
     * 姓名
     */
    private String customer_name;
    /**
     * 否支持其他证件类型:身份证-0,其他1
     */
    private String customer_ident_type  ;
    /**
     * 证件号码：
     * cert_type=0身份证号
     * cert_type=1护照号
     * cert_type=B港澳居民来往内地通行证号
     * cert_type=C台湾居民来往大陆通行证号
     */
    private String customer_ident_no;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 证件正面照下载地址：
     * cert_type=0:身份证正面
     * cert_type=1:护照带人像图片
     * cert_type=B:港澳居民来往内地通行
     * 证照带人像图片cert_type=C:台湾居民来往大陆通行证照带人像图片
     */
    private String ident_front_path;
    /**
     * 证件反面照下载地址：
     * cert_type=0:身份证反面
     * cert_type=1:护照封图片
     * cert_type=B:港澳居民来往内地通行
     * 证照封图图片cert_type=C:台湾居民来往大陆通行证照封图图片
     */
    private String ident_back_path;
    /**
     * 刷脸是否显示结果页面：
     * 参数值为“1”：直接跳转到
     * return_url或法大大指定页面
     * 参数值为“2”：需要用户点击确认后
     * 跳转到return_url或法大大指定页面
     * 参数值为“3”：认证无论是否通过，
     * 直接跳转return_url，仅对个人认证有效
     */
    private String id_photo_optional;

    /**
     * 是否认证成功后自动申请实名证书
     * 参数值为“0”：不申请
     * 参数值为“1”：自动申请
     */
    private String cert_flag;
    /**
     * add（新增）、modify（修改），不传默认add
     */
    private String option;
    /**
     * 证件类型:
     * 0：身份证；
     * 1：护照；
     * B：港澳居民来往
     * 内地通行证,
     * C：台湾居民来往
     * 大陆通行证
     * (默认为0，仅当支
     * 持其他证件时，证
     * 件类型1/B/C接口
     * 允许同步传参)
     */
    private String cert_type;
    /**
     * 个人银行卡
     */
    private String bank_card_no;
    /**
     * 是否跳转法大大公
     * 证处小程序认证，
     * 0：小程序
     * webview内嵌法大
     * 大h5（默认）；
     * 1：跳转法大大公
     * 证处小程序认证；
     */
    private String is_mini_program;
    /**
     * zh：中文；
     * en：英文
     */
    private String lang;
}
