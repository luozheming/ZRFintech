package com.service;

import com.dto.indto.PageDto;
import com.dto.outdto.PageListDto;
import com.pojo.Attention;

public interface AttentionService {
    PageListDto<Attention> pageList(PageDto pageDto);
    void add(Attention attention);
    void cancel(Attention attention);
}
