package com.service;

import com.dto.indto.PageDto;
import com.dto.outdto.PageListDto;
import com.pojo.FinancialAdvisor;

public interface FinancialAdvisorService {
    PageListDto<FinancialAdvisor> pageList(PageDto pageDto);
    void add(FinancialAdvisor financialAdvisor);
    void edit(FinancialAdvisor financialAdvisor);
    void status(FinancialAdvisor financialAdvisor);
    void delete(String faId);
}
