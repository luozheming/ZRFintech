package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.indto.PageDto;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.pojo.BrowseHistory;
import com.service.BrowseHistoryService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/browseHistory")
public class BrowseHistoryController {

    @Autowired
    private BrowseHistoryService browseHistoryService;

    @GetMapping("/pageList")
    public String pageList(PageDto pageDto) {
        if (pageDto.getPageNum() < 0 || pageDto.getPageSize() <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        PageListDto pageListDto = browseHistoryService.pageList(pageDto);
        OutputFormate outputFormate = new OutputFormate(pageListDto);
        return JSONObject.toJSONString(outputFormate);
    }

}
