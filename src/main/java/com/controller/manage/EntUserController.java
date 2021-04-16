package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.pojo.EntUser;
import com.pojo.ProjectBpApply;
import com.service.manage.EntUserService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
            int count = entUserService.count();
            int totalPage = count/pageSize;
            PageListDto pageListDto = new PageListDto<EntUser>();
            pageListDto.setTotal(count);
            if(pageNum <= totalPage){
                List<EntUser> entUsers =  entUserService.pageList(pageNum, pageSize);
                pageListDto.setList(entUsers);
            }
            OutputFormate outputFormate = new OutputFormate(pageListDto);
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

}
