package com.pojo;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(value = "investor")
public class Investor {
    /**
     * 投资人 Id
     */
    @Id
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
