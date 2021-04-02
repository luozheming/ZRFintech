package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.pojo.Project;
import com.service.manage.ProjectService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * 分页获取项目列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/pageList")
    public String pageList(Integer pageNum, Integer pageSize) {
        List<Project> projects = null;
        try {
            projects = projectService.pageList(pageNum, pageSize);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        OutputFormate outputFormate = new OutputFormate(projects, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     * 查询项目详情
     *
     * @param projectNo
     * @return
     */
    @GetMapping("/detail")
    public String detail(String projectNo) {
        Project project = null;
        try {
            project = projectService.detail(projectNo);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        OutputFormate outputFormate = new OutputFormate(project, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

}
