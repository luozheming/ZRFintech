package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.HomePageDto;
import com.dto.outdto.OutputFormate;
import com.pojo.User;
import com.service.manage.UserService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

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
//                return ErrorCode.EMPITYUSER.toJsonString();
            }

            // 获取首页展示相关信息
            homePageDto = userService.homePage();

        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        OutputFormate outputFormate = new OutputFormate(homePageDto, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

}
