package com.dto.outdto;

import lombok.Data;

import java.util.List;

@Data
public class PageListDto<T> {
    private Integer total;
    private List<T> list;
}
