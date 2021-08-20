package com.dto.indto;

import lombok.Data;

@Data
public class EntUserPageListDto {
    private Integer pageNum;
    private Integer pageSize;
    private String searchField;
}
