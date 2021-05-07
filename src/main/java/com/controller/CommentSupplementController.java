package com.controller;

import com.pojo.ProjectCommentSupplement;
import com.service.CommentSupplementService;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/commentSupplement")
public class CommentSupplementController {

    @Autowired
    private CommentSupplementService commentSupplementService;

    @Autowired
    private CommonUtils commonUtils;

    @Value("{supplementFilepath}")
    private String supplementFilepath;

    /**
     * 修改用户
     * @param file
     * @param projectCommentSupplement
     * @return
     */
    @PostMapping("/add")
    public String add(MultipartFile file, ProjectCommentSupplement projectCommentSupplement) {
        try {
            String filePath = supplementFilepath.toString();
            if (null != file) {
                commonUtils.uploadData(file, filePath);
                projectCommentSupplement.setFilePath(filePath + "/" + file.getOriginalFilename());
            }
            commentSupplementService.add(projectCommentSupplement);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }
}
