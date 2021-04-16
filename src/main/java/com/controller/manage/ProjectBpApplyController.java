package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.pojo.Investor;
import com.pojo.Project;
import com.pojo.ProjectBpApply;
import com.service.manage.ProjectBpApplyService;
import com.service.manage.ProjectService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/projectBpApply")
public class ProjectBpApplyController {

    @Autowired
    private ProjectBpApplyService projectBpApplyService;

    /**
     * 分页获取项目列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/pageListByEnt")
    public String pageListByEnt(Integer pageNum, Integer pageSize, String openId) {
        if (pageNum < 0 || pageSize <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        try {
            int count = projectBpApplyService.countByEnt(openId);
            int totalPage = count/pageSize;
            PageListDto pageListDto = new PageListDto<ProjectBpApply>();
            pageListDto.setTotal(count);
            if(pageNum <= totalPage){
                List<ProjectBpApply> projectBpApplyList =  projectBpApplyService.pageListByEnt(pageNum, pageSize, openId);
                pageListDto.setList(projectBpApplyList);
            }
            OutputFormate outputFormate = new OutputFormate(pageListDto);
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

}
