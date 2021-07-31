package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.indto.PdfDto;
import com.dto.outdto.ActivityDto;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.pojo.Activity;
import com.pojo.ActivityRecord;
import com.service.ActivityRecordService;
import com.service.ActivityService;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import com.utils.PdfUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityRecordService activityRecordService;
    @Autowired
    private CommonUtils commonUtils;
    @Value("${activityPhotoSavedFilepath}")
    private String photoPath;
    @Value("${s3BucketName}")
    private String s3BucketName;
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping("/add")
    public String add(MultipartFile photoFile, Activity activity) {
        try {
            if (null != photoFile) {
                // AWS S3存储文件
                commonUtils.uploadFile(s3BucketName,photoPath + photoFile.getOriginalFilename(), photoFile.getBytes());
                activity.setPhotoRoute(photoPath + photoFile.getOriginalFilename());
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
                // AWS S3存储文件
                commonUtils.uploadFile(s3BucketName,photoPath + photoFile.getOriginalFilename(), photoFile.getBytes());
                activity.setPhotoRoute(photoPath + photoFile.getOriginalFilename());
            }
            activityService.edit(activity);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @GetMapping("/pageList")
    public String pageList(Integer pageNum, Integer pageSize, Integer activityType, Integer status, String userId) {
        if (pageNum < 0 || pageSize <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        try {
            int count = activityService.count(activityType);
            int totalPage = count/pageSize;
            PageListDto pageListDto = new PageListDto<ActivityDto>();
            pageListDto.setTotal(count);
            if(pageNum <= totalPage){
                List<Activity> activities =  activityService.pageList(pageNum, pageSize, activityType, status);
                List<ActivityDto> activityDtoList = new ArrayList<>();
                ActivityDto activityDto = null;
                if (!CollectionUtils.isEmpty(activities)) {
                    for (Activity activity : activities) {
                        activityDto = new ActivityDto();
                        BeanUtils.copyProperties(activity, activityDto);
                        ActivityRecord activityRecord = activityRecordService.detailByUserId(userId, activity.getId());
                        if (null != activityRecord) {
                            activityDto.setIsParticipant(true);
                        } else {
                            activityDto.setIsParticipant(false);
                        }
                        activityDtoList.add(activityDto);
                    }
                }
                pageListDto.setList(activityDtoList);
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
//                // AWS S3存储文件
//                commonUtils.uploadFile(s3BucketName,photoPath + file.getOriginalFilename(), file.getBytes());
//                article.setPhotoRoute(cdnDomainName + "/" + photoPath + file.getOriginalFilename());
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

    /**
     * 下载模拟路演演讲稿
     * @param response
     * @param pdfDtoListJson
     * @return
     */
    @GetMapping("/downloadRoadShowFile")
    public String downloadRoadShowFile(HttpServletResponse response, @RequestParam String pdfDtoListJson) {
        OutputStream out = null;
        try {
            List<PdfDto> pdfDtoList = JSONObject.parseArray(pdfDtoListJson, PdfDto.class);
            ByteArrayOutputStream baos = PdfUtil.createPdf(pdfDtoList);
            // 将文件名称进行编码
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(new StringBuilder(pdfDtoList.get(0).getTitle()).append(".pdf").toString(), "UTF-8"));
            response.setContentLength(baos.size());
            out = response.getOutputStream();
            baos.writeTo(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        return ErrorCode.SUCCESS.toJsonString();
    }

    /**
     * 测试关联查询
     * @return
     */
    @GetMapping("/testUnion")
    public String testUnion() {
        Criteria criteria = new Criteria();
        criteria.and("activityType").is(1);
        criteria.and("activityRecord.participantName").is("王政嘉");
        LookupOperation lookup = LookupOperation.newLookup()
                //从表（关联的表）
                .from("activityRecord")
                //主表中与从表相关联的字段
                .localField("id")
                //从表与主表相关联的字段
                .foreignField("activityId")
                //查询出的从表集合 命名
                .as("activityRecord");
        AggregationOperation operation = Aggregation.match(criteria);

        Aggregation agg = Aggregation.newAggregation(lookup, Aggregation.unwind("activityRecord"), operation);
        AggregationResults<Map> studentAggregation = mongoTemplate.aggregate(agg, "activity", Map.class);

        return JSONObject.toJSONString(studentAggregation);
    }
}
