package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.indto.EntUserPageListDto;
import com.dto.indto.PageDto;
import com.dto.outdto.*;
import com.pojo.ProjectDeliver;
import com.pojo.User;
import com.pojo.VIPCardUsage;
import com.service.ProjectDeliverService;
import com.service.VIPCardService;
import com.service.VIPCardUsageService;
import com.service.manage.UserService;
import com.utils.DateUtil;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private VIPCardUsageService vipCardUsageService;
    @Autowired
    private ProjectDeliverService projectDeliverService;

    /**
     * 管理员用户登录
     * @param phoneNm
     * @param password
     * @return
     */
    @PostMapping("/login")
    public String login(String phoneNm, String password) {

        if (StringUtils.isEmpty(phoneNm) || StringUtils.isEmpty(password)) {
            return ErrorCode.EMPITYPHONE.toJsonString();
        }

        try {
            User user = userService.login(phoneNm, password);
            if (null == user) {
                return ErrorCode.EMPITYUSER.toJsonString();
            } else {
                OutputFormate outputFormate = new OutputFormate(user, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
                return JSONObject.toJSONString(outputFormate);
            }
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 管理员首页信息展示
     * @param userId
     * @return
     */
    @GetMapping("/homePage")
    public String homePage(@RequestParam(value = "userId", required = true) String userId) {
        HomePageDto homePageDto = new HomePageDto();
        try {
            // 检验用户是否存在
            User user = userService.getById(userId);
            if (null == user) {
                return ErrorCode.EMPITYUSER.toJsonString();
            }

            // 获取首页展示相关信息
            homePageDto = userService.homePage();

        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        OutputFormate outputFormate = new OutputFormate(homePageDto, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     * 同步历史数据
     * @param userId
     * @return
     */
    @GetMapping("/synchroHistData")
    public String synchroHistData(@RequestParam(value = "userId", required = false) String userId) {
        try {
            // 检验用户是否存在
            User user = userService.getById(userId);
            if (null == user || !"management".equals(user.getRoleCode())) {
                return ErrorCode.EMPITYUSER.toJsonString();
            }
            userService.synchroHistData();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        return ErrorCode.SUCCESS.toJsonString();
    }

    /**
     * 获取企业用户信息
     * @param entUserPageListDto
     * @return
     */
    @GetMapping("/entUserPageList")
    public String entUserPageList(EntUserPageListDto entUserPageListDto) {
        try {
            int count = userService.entUserCount(entUserPageListDto);
            int totalPage = count/entUserPageListDto.getPageSize();
            PageListDto pageListDto = new PageListDto<EntUserDto>();
            pageListDto.setTotal(count);
            if(entUserPageListDto.getPageNum() <= totalPage){
                List<EntUserDto> entUserDtoList = userService.entUserPageList(entUserPageListDto);
                if (!CollectionUtils.isEmpty(entUserDtoList)) {
                    VIPCardUsageRespDto vipCardUsageRespDto = null;
                    for (EntUserDto entUserDto : entUserDtoList) {
                        // 查看是否会员
                        vipCardUsageRespDto = vipCardUsageService.detailByUserId(entUserDto.getUserId());
                        if (null != vipCardUsageRespDto && null != vipCardUsageRespDto.getEndTime() && DateUtil.getDiffDate(vipCardUsageRespDto.getEndTime(), new Date(), 1) > 0) {
                            entUserDto.setIsVipCard(true);
                        } else {
                            entUserDto.setIsVipCard(false);
                        }
                        // 查看是否存在BP投递记录
                        int n = projectDeliverService.count(entUserDto.getUserId());
                        if (n > 0) {
                            entUserDto.setIsDeliver(true);
                        } else {
                            entUserDto.setIsDeliver(false);
                        }
                    }
                }
                pageListDto.setList(entUserDtoList);
            }
            OutputFormate outputFormate = new OutputFormate(pageListDto);
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 取消用户认证
     * @param userId
     * @return
     */
    @PostMapping("/cancelVerify")
    public String cancelVerify(String userId) {
        userService.cancelVerify(userId);
        return ErrorCode.SUCCESS.toJsonString();
    }


    /**
     * 获取企业用户信息
     * @param pageDto
     * @return
     */
    @GetMapping("/pageList")
    public String pageList(PageDto pageDto) {
        if (pageDto.getPageNum() < 0 || pageDto.getPageSize() <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        try {
            PageListDto<User> pageListDto = userService.pageList(pageDto);
            OutputFormate outputFormate = new OutputFormate(pageListDto);
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @PostMapping("/auditUser")
    public String auditUser(String userId, Integer auditStatus) {
        userService.auditUser(userId, auditStatus);
        return ErrorCode.SUCCESS.toJsonString();
    }

    @GetMapping("/delete")
    public String delete(String userId, String phoneNm) {
        userService.delete(userId, phoneNm);
        return ErrorCode.SUCCESS.toJsonString();
    }
}
