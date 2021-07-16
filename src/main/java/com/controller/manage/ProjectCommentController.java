package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.pojo.ProjectComment;
import com.service.manage.ProjectCommentService;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/projectComment")
public class ProjectCommentController {

    @Autowired
    private ProjectCommentService projectCommentService;
    @Autowired
    private CommonUtils commonUtils;
    @Value("${questionFilePath}")
    private String questionFilePath;
    @Value("${s3BucketName}")
    private String s3BucketName;

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
        try {
            int count = projectCommentService.count();
            int totalPage = count/pageSize;
            PageListDto pageListDto = new PageListDto<ProjectComment>();
            pageListDto.setTotal(count);
            if(pageNum <= totalPage){
                List<ProjectComment> projectComments =  projectCommentService.pageList(pageNum, pageSize);
                pageListDto.setList(projectComments);
            }
            OutputFormate outputFormate = new OutputFormate(pageListDto, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
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

    /**
     * 新增追问信息
     * @param file
     * @param projectComment
     * @return
     */
    @PostMapping("/add")
    public String add(MultipartFile file,  ProjectComment projectComment) {
        try {
            String filePath = questionFilePath;
            if (null != file) {
                // AWS S3存储文件
                commonUtils.uploadFile(s3BucketName,filePath + file.getOriginalFilename(), file.getBytes());
                projectComment.setQuestionFilePath(filePath + file.getOriginalFilename());
            }
            projectCommentService.add(projectComment);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        return ErrorCode.SUCCESS.toJsonString();
    }

    /**
     * 删除评论
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public String delete(@RequestParam String id) {
        try {
            projectCommentService.delete(id);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        return ErrorCode.SUCCESS.toJsonString();
    }

    /**
     * 修改评论的状态
     * @param id
     * @param status
     * @return
     */
    @PostMapping("/status")
    public String status(String id, Integer status) {
        projectCommentService.status(id, status);
        return ErrorCode.SUCCESS.toJsonString();
    }

}
