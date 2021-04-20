package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.pojo.ProjectBpApply;
import com.service.manage.ProjectBpApplyService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
     * 分页获取项目BP申请列表
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

    /**
     * 查询BP申请详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detailById")
    public String detailById(String id) {
        if (StringUtils.isEmpty(id)) {
            return ErrorCode.NULLPARAM.toJsonString();
        }
        try {
            ProjectBpApply projectBpApply = projectBpApplyService.detailById(id);
            OutputFormate outputFormate = new OutputFormate(projectBpApply);
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

}
