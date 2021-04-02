package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.pojo.EntUser;
import com.service.manage.EntUserService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/entUser")
public class EntUserController {

    @Autowired
    private EntUserService entUserService;

    /**
     * 获取企业用户信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/pageList")
    public String pageList(Integer pageNum, Integer pageSize) {
        List<EntUser> entUsers = null;
        try {
            entUsers = entUserService.pageList(pageNum, pageSize);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        OutputFormate outputFormate = new OutputFormate(entUsers, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

}
