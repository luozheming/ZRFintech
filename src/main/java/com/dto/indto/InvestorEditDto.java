package com.dto.indto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class InvestorEditDto {
    /**
     * 用户ID，主键
     */
    private String userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 手机号码
     */
    private String phoneNm;
    /**
     * 公司
     */
    private String companyName;
    /**
     * 职称
     */
    private String positionName;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     *  联系电话号码
     */
    private String telephoneNo;
    /**
     * 头像文件
     */
    private MultipartFile photoFile;
    /**
     * 名片正面文件
     */
    private MultipartFile cardFile;
    /**
     * 名片反面文件
     */
    private MultipartFile cardBackFile;

}
