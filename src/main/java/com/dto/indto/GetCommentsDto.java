package com.dto.indto;

import lombok.Data;

@Data
public class GetCommentsDto {
    /**
     * 投资人姓名
     */
    private String investorId;
    /**
     *用户唯一识别键
     */
    private String openId;
    /**
     * 页数
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;
}
