package com.service;

import com.dto.indto.PageDto;
import com.dto.outdto.PageListDto;
import com.pojo.Collection;

public interface CollectionService {
    PageListDto<Collection> pageList(PageDto pageDto);
    void add(Collection collection);
    void cancel(Collection collection);
}
