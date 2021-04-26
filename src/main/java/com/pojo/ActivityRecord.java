package com.pojo;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection = "activityRecord")
public class ActivityRecord {
    /**
     * 主键id
     */
    private String id;
    /**
     * 主题
     */
    private String theme;
    /**
     * 开展日期 格式；YYYYMMDD
     */
    private String startDate;
    /**
     * 参与者姓名
     */
    private String participantName;
    /**
     * 参与者公司名称
     */
    private String participantCompanyName;
    /**
     * 参与者联系电话
     */
    private String participantPhoneNm;
    /**
     * 创建日期
     */
    private Date createTime;

}
