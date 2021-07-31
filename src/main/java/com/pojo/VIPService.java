package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(value = "vipService")
public class VIPService {
    /**
     * 主键id
     */
    private String id;
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务内容
     */
    private String serviceContent;
    /**
     * 可使用次数
     */
    private Integer times;
    /**
     * 排列顺序
     */
    private Integer orderNo;

    @Tolerate
    public VIPService() {}
}
