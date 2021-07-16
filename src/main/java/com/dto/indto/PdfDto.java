package com.dto.indto;

import lombok.Data;

@Data
public class PdfDto {

    /**
     * 文章标题
     */
    private String title;
    /**
     * 正文小标题
     */
    private String head;
    /**
     * 正文内容
     */
    private String text;

}
