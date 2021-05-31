package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.pojo.Activity;
import com.pojo.Project;
import com.service.ActivityService;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private CommonUtils commonUtils;
    @Value("${activityPhotoSavedFilepath}")
    private String photoPath;

    @PostMapping("/add")
    public String add(MultipartFile photoFile, Activity activity) {
        try {
            if (null != photoFile) {
                commonUtils.uploadData(photoFile, photoPath);
                activity.setPhotoRoute(photoPath + "/" + photoFile.getOriginalFilename());
            }
            activityService.add(activity);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @PostMapping("/edit")
    public String edit(MultipartFile photoFile, Activity activity) {
        try {
            if (null != photoFile) {
                commonUtils.uploadData(photoFile, photoPath);
                activity.setPhotoRoute(photoPath + "/" + photoFile.getOriginalFilename());
            }
            activityService.edit(activity);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @GetMapping("/pageList")
    public String pageList(Integer pageNum, Integer pageSize, Integer activityType) {
        if (pageNum < 0 || pageSize <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        try {
            int count = activityService.count(activityType);
            int totalPage = count/pageSize;
            PageListDto pageListDto = new PageListDto<Project>();
            pageListDto.setTotal(count);
            if(pageNum <= totalPage){
                List<Activity> activities =  activityService.pageList(pageNum, pageSize, activityType);
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
        Activity activity = activityService.detail(id);
        OutputFormate outputFormate = new OutputFormate(activity, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    @PostMapping("/uploadFile")
    public String uploadFile(MultipartFile file) {
        try {
            if (null != file) {
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
        activityService.delete(id);
        return ErrorCode.SUCCESS.toJsonString();
    }

    @GetMapping("/draft")
    public String draft() {
        Activity activity = activityService.draft();
        OutputFormate outputFormate = new OutputFormate(activity, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

}
