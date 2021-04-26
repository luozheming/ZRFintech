package com.controller;

import com.pojo.ActivityRecord;
import com.service.ActivityRecordService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
