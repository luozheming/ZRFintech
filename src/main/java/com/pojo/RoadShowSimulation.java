package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(value = "roadShowSimulation")
public class RoadShowSimulation {
    /**
     * 主键id
     */
    private String id;
    /**
     * 模拟总时长
     */
    private long totalDuration;
    /**
     * 阶段持续时长
     */
    private long duration;
    /**
     * 文字提示
     */
    private String wordPrompt;
    /**
     * 语音提示音频路径
     */
    private String voicePromptPath;
    /**
     * 语音提示描述
     */
    private String voicePromptDesc;
    /**
     * 模拟路演说明
     */
    private String describe;
    /**
     * 创建时间
     */
    private Date createTime;

    @Tolerate
    public RoadShowSimulation() {}

}
