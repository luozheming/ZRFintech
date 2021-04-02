package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.pojo.ProjectComment;
import com.service.manage.ProjectCommentService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/projectComment")
public class ProjectCommentController {

    @Autowired
    private ProjectCommentService projectCommentService;

    /**
     * 分页获取项目评论列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/pageList")
    public String pageList(Integer pageNum, Integer pageSize) {
        //参数判断
        if (pageNum < 0 || pageSize <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }

        List<ProjectComment> projectComments = null;
        try {
            projectComments = projectCommentService.pageList(pageNum, pageSize);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        OutputFormate outputFormate = new OutputFormate(projectComments, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     * 提交评论
     * @param projectComment
     * @return
     */
    @PostMapping("/commit")
    public String commit(@RequestBody ProjectComment projectComment) {
        try {
            projectCommentService.commit(projectComment);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        return ErrorCode.SUCCESS.toJsonString();
    }

}
