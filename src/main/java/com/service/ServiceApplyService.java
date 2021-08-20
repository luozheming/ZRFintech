package com.service;

import com.dto.indto.PageDto;
import com.dto.outdto.PageListDto;
import com.pojo.ServiceApply;

public interface ServiceApplyService {
    PageListDto<ServiceApply> pageList(PageDto pageDto);
    void add(ServiceApply serviceApply);
}
