package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.pojo.ActivityRecord;
import com.pojo.Project;
import com.service.ActivityRecordService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activityRecord")
public class ActivityRecordController {

    @Autowired
    private ActivityRecordService activityRecordService;

    @PostMapping("/add")
    public String add(@RequestBody ActivityRecord activityRecord) {
        ActivityRecord activityRecordResp = activityRecordService.detailByPhoneNm(activityRecord.getParticipantPhoneNm());
        if (null != activityRecordResp) {
            return ErrorCode.REPEATCOMMIT.toJsonString();
        }
        activityRecordService.add(activityRecord);
        return ErrorCode.SUCCESS.toJsonString();
    }

    @GetMapping("/pageList")
    public String pageList(Integer pageNum, Integer pageSize) {
        if (pageNum < 0 || pageSize <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        try {
            int count = activityRecordService.count();
            int totalPage = count/pageSize;
            PageListDto pageListDto = new PageListDto<Project>();
            pageListDto.setTotal(count);
            if(pageNum <= totalPage){
                List<ActivityRecord> activityRecords =  activityRecordService.pageList(pageNum, pageSize);
                pageListDto.setList(activityRecords);
            }
            OutputFormate outputFormate = new OutputFormate(pageListDto);
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

}
