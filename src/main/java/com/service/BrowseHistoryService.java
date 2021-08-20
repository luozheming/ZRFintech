package com.service;

import com.dto.indto.PageDto;
import com.dto.outdto.PageListDto;
import com.pojo.BrowseHistory;

public interface BrowseHistoryService {
    PageListDto<BrowseHistory> pageList(PageDto pageDto);
    void add(BrowseHistory browseHistory);
    void delete(String id);
    void clear(String userId);
}
