package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.EntUserDto;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.service.manage.EntUserService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
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
        try {
            PageListDto<EntUserDto> pageListDto = entUserService.pageList(pageNum, pageSize);
            OutputFormate outputFormate = new OutputFormate(pageListDto);
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getMessage());
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

}
