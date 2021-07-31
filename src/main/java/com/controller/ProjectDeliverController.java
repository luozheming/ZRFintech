package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OrderOutDto;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.pojo.ProjectDeliver;
import com.service.ProjectDeliverService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projectDeliver")
public class ProjectDeliverController {

    @Autowired
    private ProjectDeliverService projectDeliverService;

    /**
     * 新增项目群发记录
     * @param projectDeliver
     * @return
     */
    @PostMapping("/add")
    public String add(@RequestBody ProjectDeliver projectDeliver) {
        try {
            projectDeliverService.add(projectDeliver);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 新增项目群发记录
     * @param userId
     * @return
     */
    @GetMapping("/pageListByUserId")
    public String pageListByUserId(Integer pageNum, Integer pageSize, String userId) {
        if (pageNum < 0 || pageSize <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        try {
            int count = projectDeliverService.count(userId);
            int totalPage = count/pageSize;
            PageListDto pageListDto = new PageListDto<ProjectDeliver>();
            pageListDto.setTotal(count);
            if(pageNum <= totalPage){
                List<ProjectDeliver> projectDeliverList = projectDeliverService.pageListByUserId(pageNum, pageSize, userId);
                pageListDto.setList(projectDeliverList);
            }
            OutputFormate outputFormate = new OutputFormate(pageListDto);
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

}
