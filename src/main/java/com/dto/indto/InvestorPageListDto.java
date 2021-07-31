package com.dto.indto;

import lombok.Data;

@Data
public class InvestorPageListDto {

    private Integer pageNum;
    private Integer pageSize;
    /**
     * 投资领域（行业）
     */
    private String focusFiled;
    /**
     * 投资轮次
     */
    private String finRound;
    /**
     * 所在城市
     */
    private String city;

}
