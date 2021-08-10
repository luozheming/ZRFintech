package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.dto.outdto.ProjectDto;
import com.pojo.Project;
import com.service.manage.ProjectService;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private CommonUtils commonUtils;
    @Value("${savedfilepath}")
    private String savedfilepath;
    @Value("${s3BucketName}")
    private String s3BucketName;

    /**
     * 分页获取项目列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/pageList")
    public String pageList(Integer pageNum, Integer pageSize) {
        if (pageNum < 0 || pageSize <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        try {
            int count = projectService.count();
            int totalPage = count/pageSize;
            PageListDto pageListDto = new PageListDto<Project>();
            pageListDto.setTotal(count);
            if(pageNum <= totalPage){
                List<Project> projects =  projectService.pageList(pageNum, pageSize);
                pageListDto.setList(projects);
            }
            OutputFormate outputFormate = new OutputFormate(pageListDto);
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 查询项目详情
     *
     * @param projectNo
     * @return
     */
    @GetMapping("/detail")
    public String detail(String projectNo) {
        ProjectDto projectDto = null;
        try {
            projectDto = projectService.detail(projectNo);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        OutputFormate outputFormate = new OutputFormate(projectDto, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     * 通过projectNo删除项目
     * @param projectNo
     * @return
     */
    @DeleteMapping("/delete")
    public String delete(@RequestParam String projectNo) {

        projectService.delete(projectNo);
        return ErrorCode.SUCCESS.toJsonString();
    }

    @PostMapping("/status")
    public String status(String projectNo, Integer status) {
        projectService.status(projectNo, status);
        return ErrorCode.SUCCESS.toJsonString();
    }

    @PostMapping("/edit")
    public String edit(MultipartFile file, MultipartFile logoFile, Project project) {
        try {
            //文件上传可能会出问题
            if (null != file) {
                // 获取文件名
                String fileName = file.getOriginalFilename();
                // 文件上传后的路径
                StringBuilder filePathBuffer = new StringBuilder();
                String filePath = filePathBuffer.append(savedfilepath).append(project.getEntUserId()).append("/").append(project.getProjectNo()).append("/").toString();
                try {
                    // AWS S3存储文件
                    commonUtils.uploadFile(s3BucketName,filePath + file.getOriginalFilename(), file.getBytes());
                    //文件保存后更新数据库信息
                    project.setBpRoute(filePath + fileName);
                } catch (Exception e) {
                    return ErrorCode.FILEUPLOADFAILED.toJsonString();
                }
            }

            //文件上传可能会出问题
            if (null != logoFile) {
                // 获取文件名
                String fileName = logoFile.getOriginalFilename();
                // 文件上传后的路径
                StringBuilder filePathBuffer = new StringBuilder();
                String filePath = filePathBuffer.append(savedfilepath).append(project.getEntUserId()).append("/").append(project.getProjectNo()).append("/").toString();
                try {
                    // AWS S3存储文件
                    commonUtils.uploadFile(s3BucketName,filePath + logoFile.getOriginalFilename(), logoFile.getBytes());
                    //文件保存后更新数据库信息
                    project.setLogoRoute(filePath + fileName);
                } catch (Exception e) {
                    return ErrorCode.FILEUPLOADFAILED.toJsonString();
                }
            }
            projectService.edit(project);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        return ErrorCode.SUCCESS.toJsonString();
    }

    @PostMapping("/topProject")
    public String topProject(String userId) {
        try {
            projectService.topProject(userId);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            OutputFormate outputFormate = new OutputFormate("", ErrorCode.OTHEREEEOR.getCode(), e.getMessage());
            return JSONObject.toJSONString(outputFormate);
        }
    }

}
