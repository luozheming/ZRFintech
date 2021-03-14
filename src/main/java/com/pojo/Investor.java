package com.pojo;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.bson.types.ObjectId;

@Data
@Builder
public class Investor {
    /**
     * Document Id
     */
    private ObjectId id;
    /**
     * 投资人Id
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
     * 投资人邮箱
     */
    private String invesEmail;
    /**
     * 行业标签1
     */
    private String indusLab1;

    @Tolerate
    public Investor() {}
}
