package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Document(value = "investor")
public class Investor {
    /**
     * 投资人 Id（同user表的userId）
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
     *  投资人名片路径
     */
    private String invesCardRoute;
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
     *投资人名片数据
     */
    private String cardPhoto;
    /**
     * 机构图片数据
     */
    private String orgPhoto;
    /**
     *  投资人线上问答资费原价
     */
    private BigDecimal price;
    /**
     * 投资人线上问答资费折扣价
     */
    private BigDecimal disCountPrice;
    /**
     *  投资人1V1交流（线上）资费原价
     */
    private BigDecimal onlinePrice;
    /**
     * 投资人1V1交流（线上）资费折扣价
     */
    private BigDecimal onlineDisCountPrice;
    /**
     *  投资人1V1交流（线下）资费原价
     */
    private BigDecimal offlinePrice;
    /**
     * 投资人1V1交流（线下）资费折扣价
     */
    private BigDecimal offlineDisCountPrice;
    /**
     *  定制商业计划书资费原价
     */
    private BigDecimal bpMadePrice;
    /**
     * 定制商业计划书资费折扣价
     */
    private BigDecimal bpMadeDisCountPrice;
    /**
     * 追问价格
     */
    private BigDecimal questionPrice;
    /**
     * 投资人未获取的资费金额
     */
    private BigDecimal unaccomplishedAmount;
    /**
     * 投资人已获取的资费金额
     */
    private BigDecimal accomplishedAmount;
    /**
     * 投资人剩余金额
     */
    private BigDecimal surplusAmount;
    /**
     * 是否平台投资人：true-是，false-否
     */
    private Boolean isPlatform;
    /**
     * 状态：0-有效，1-失效
     */
    private Integer status;
    /**
     * 关注行业
     */
    private String focusFiled;
    /**
     * 投资轮次
     */
    private String finRound;
    /**
     * 自我简介
     */
    private String selfIntroduction;
    /**
     * 已成交单数
     */
    private Integer accomplishedTimes;
    /**
     * 未成交单数
     */
    private Integer unAccomplishedTimes;
    /**
     * 星级
     */
    private BigDecimal stars;
    /**
     * 实际星级值
     */
    private BigDecimal actualStars;
    /**
     * 内部星级加权值
     */
    private BigDecimal internalWeightingStars;
    /**
     * 所在城市
     */
    private String city;
    /**
     * 关注城市
     */
    private String focusCity;
    /**
     * 数据来源说明，如；excel导入，后台单个录入
     */
    private String sourceDesc;
    /**
     * 投资人展示标识：0-不展示，1-投资人授权展示
     */
    private Integer showFlag;
    /**
     * 是否认证投资人
     */
    private Boolean isVerify;
    /**
     * 是否被关注
     */
    private Boolean isAttention;
    /**
     * 创建时间
     */
    private Date CreateTime;

    @Tolerate
    public Investor() {}
}
