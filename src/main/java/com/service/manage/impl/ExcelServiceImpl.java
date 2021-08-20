package com.service.manage.impl;

import com.pojo.FinancialAdvisor;
import com.pojo.Investor;
import com.service.manage.ExcelService;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String photoRoute = "/home/ec2-user/data/investor/默认投资人.jpg";
    private static final Logger logger = LoggerFactory.getLogger(ExcelServiceImpl.class);

    @Override
    public void importInvestor(MultipartFile file) throws Exception {
        // 1，创建Excel工作簿
        Workbook workbook = getWorkBook(file);
        if (null == workbook) {
            throw new Exception(ErrorCode.EMPITYFILE.getMessage());
        }

        // 2，解析excel具体信息
        Sheet sheet = workbook.getSheetAt(0);// 获取excel第一个表单
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        Row row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        List<Investor> investorList = new ArrayList<>();
        Investor investor = null;
        if (rowNum > 1 && colNum > 0) {
            // 正文内容应该从第二行开始,第一行为表头的标题
            for (int i = 1; i < rowNum; i++) {
                row = sheet.getRow(i);
                investor = new Investor();
                investor.setInvestor(String.valueOf(getCellFormatValue(row.getCell(0))));// 投资人姓名
                // 机构名称=机构名称|职位|城市(excel暂时未维护所在城市字段)
                String orgNm = String.valueOf(getCellFormatValue(row.getCell(2))) + "  |  " + String.valueOf(getCellFormatValue(row.getCell(1)));
                investor.setOrgNm(orgNm);
                investor.setInvesEmail(String.valueOf(getCellFormatValue(row.getCell(3))));// 联系方式（邮箱）
                investor.setFocusFiled(String.valueOf(getCellFormatValue(row.getCell(4))).replaceAll("，|、", ","));// 关注行业
                investor.setFinRound(String.valueOf(getCellFormatValue(row.getCell(5))).replaceAll("，|、", ","));// 关注轮次
                investor.setFocusCity(String.valueOf(getCellFormatValue(row.getCell(6))).replaceAll("，|、", ","));// 关注城市
                investor.setInvestorId(commonUtils.getNumCode());
                investor.setInvesPhotoRoute(photoRoute);
                investor.setSourceDesc("excel批量导入");
                investor.setStatus(0);

                investorList.add(investor);
            }

            // 3，批量插入投资人信息
            mongoTemplate.insert(investorList, Investor.class);
        }

    }

    @Override
    public void importFinancialAdvisor(MultipartFile file) throws Exception {
        // 1，创建Excel工作簿
        Workbook workbook = getWorkBook(file);
        if (null == workbook) {
            throw new Exception(ErrorCode.EMPITYFILE.getMessage());
        }

        // 2，解析excel具体信息
        Sheet sheet = workbook.getSheetAt(0);// 获取excel第一个表单
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        Row row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        List<FinancialAdvisor> financialAdvisorList = new ArrayList<>();
        FinancialAdvisor financialAdvisor = null;
        if (rowNum > 1 && colNum > 0) {
            // 正文内容应该从第二行开始,第一行为表头的标题
            for (int i = 1; i < rowNum; i++) {
                row = sheet.getRow(i);
                if (StringUtils.isEmpty(String.valueOf(getCellFormatValue(row.getCell(0))))) {
                    break;
                }
                financialAdvisor = new FinancialAdvisor();
                financialAdvisor.setFaName(String.valueOf(getCellFormatValue(row.getCell(0))));// FA机构
                financialAdvisor.setOrgNm(String.valueOf(getCellFormatValue(row.getCell(0))));// FA机构
                financialAdvisor.setFocusFiled(String.valueOf(getCellFormatValue(row.getCell(1))).replaceAll("，|、", ","));// 关注行业
                financialAdvisor.setFinRound(String.valueOf(getCellFormatValue(row.getCell(2))).replaceAll("，|、", ","));// 关注轮次
                financialAdvisor.setIntrod(String.valueOf(getCellFormatValue(row.getCell(3))));// 简介
                financialAdvisor.setInvestmentCase(String.valueOf(getCellFormatValue(row.getCell(4))).replaceAll("，|、", ","));// 投资案例
                financialAdvisor.setContactUser(String.valueOf(getCellFormatValue(row.getCell(5))));// 联系人
                financialAdvisor.setTelephoneNo(String.valueOf(getCellFormatValue(row.getCell(6))));// 联系方式
                financialAdvisor.setEmail(String.valueOf(getCellFormatValue(row.getCell(7))));// 邮箱
                financialAdvisor.setCity(String.valueOf(getCellFormatValue(row.getCell(8))).replaceAll("，|、", ","));// 办公城市
                financialAdvisor.setFaId(commonUtils.getNumCode());
                financialAdvisor.setSourceDesc("excel批量导入");
                financialAdvisor.setStatus(0);
                financialAdvisor.setFaType(2);// fa类型：1-fa个人,2-fa机构
                financialAdvisor.setCreateTime(new Date());

                financialAdvisorList.add(financialAdvisor);
            }

            // 3，批量插入投资人信息
            mongoTemplate.insert(financialAdvisorList, FinancialAdvisor.class);
        }

    }

    private Workbook getWorkBook(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf("."));
        Workbook wb = null;
        try {
            InputStream is = file.getInputStream();
            if (".xls".equals(ext)) {
                wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(ext)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = null;
            }
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        }
        return wb;
    }

    //根据Cell类型设置数据
    private static Object getCellFormatValue(Cell cell) {
        Object cellvalue = "";
        if (cell != null) {
            switch (cell.getCellTypeEnum()) {
                case NUMERIC:
                    cellvalue = String.valueOf(cell.getNumericCellValue());
                    break;
                case FORMULA: {
                    cellvalue = cell.getDateCellValue();
                    break;
                }
                case STRING:
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                default:
                    cellvalue = "";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }
}
