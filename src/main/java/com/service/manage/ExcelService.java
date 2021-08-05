package com.service.manage;

import org.springframework.web.multipart.MultipartFile;

public interface ExcelService {
    void importInvestor(MultipartFile file) throws Exception;
}
