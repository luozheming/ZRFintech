package com.controller.manage;

import com.service.manage.ExcelService;
import com.utils.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    private static final Logger logger = LoggerFactory.getLogger(ExcelController.class);

    @PostMapping("/importInvestor")
    public String importInvestor(MultipartFile file) {
        try {
            excelService.importInvestor(file);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            logger.error("excel导入投资人信息系统错误：", e);
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }
}
