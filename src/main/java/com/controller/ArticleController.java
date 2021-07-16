package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.pojo.Article;
import com.pojo.Project;
import com.service.ArticleService;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommonUtils commonUtils;
    @Value("${articlePhotoSavedFilepath}")
    private String photoPath;
    @Value("${s3BucketName}")
    private String s3BucketName;

    @PostMapping("/add")
    public String add(MultipartFile photoFile, Article article) {
        try {
            if (null != photoFile) {
                // AWS S3存储文件
                commonUtils.uploadFile(s3BucketName,photoPath + photoFile.getOriginalFilename(), photoFile.getBytes());
                article.setPhotoRoute(photoPath + photoFile.getOriginalFilename());
            }
            articleService.add(article);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @PostMapping("/edit")
    public String edit(MultipartFile photoFile, Article article) {
        try {
            if (null != photoFile) {
                // AWS S3存储文件
                commonUtils.uploadFile(s3BucketName,photoPath + photoFile.getOriginalFilename(), photoFile.getBytes());
                article.setPhotoRoute(photoPath + photoFile.getOriginalFilename());
            }
            articleService.edit(article);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @GetMapping("/pageList")
    public String pageList(Integer pageNum, Integer pageSize, Integer articleType) {
        if (pageNum < 0 || pageSize <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        try {
            int count = articleService.count(articleType);
            int totalPage = count/pageSize;
            PageListDto pageListDto = new PageListDto<Project>();
            pageListDto.setTotal(count);
            if(pageNum <= totalPage){
                List<Article> activities =  articleService.pageList(pageNum, pageSize, articleType);
                pageListDto.setList(activities);
            }
            OutputFormate outputFormate = new OutputFormate(pageListDto);
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @GetMapping("/detail")
    public String detail(String id) {
        Article article = articleService.detail(id);
        OutputFormate outputFormate = new OutputFormate(article, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    @PostMapping("/uploadFile")
    public String uploadFile(MultipartFile file) {
        try {
            if (null != file) {
//                // AWS S3存储文件
//                commonUtils.uploadFile(s3BucketName,photoPath + photoFile.getOriginalFilename(), photoFile.getBytes());
//                article.setPhotoRoute(cdnDomainName + "/" + photoPath + photoFile.getOriginalFilename());
                commonUtils.uploadData(file, photoPath);
            }
            String fileData = commonUtils.getPhoto(photoPath + "/" + file.getOriginalFilename());
            return JSONObject.toJSONString(fileData);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @DeleteMapping("/delete")
    public String delete(String id) {
        articleService.delete(id);
        return ErrorCode.SUCCESS.toJsonString();
    }

    @GetMapping("/draft")
    public String draft() {
        Article article = articleService.draft();
        OutputFormate outputFormate = new OutputFormate(article, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

}
