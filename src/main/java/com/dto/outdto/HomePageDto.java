package com.dto.outdto;

import lombok.Data;

@Data
public class HomePageDto {
    /**
     * 企业用户数
     */
    private Integer entUserCount;
    /**
     * BP数
     */
    private Integer bpCount;
    /**
     * 付费用户数
     */
    private Integer isPayCount;

}
