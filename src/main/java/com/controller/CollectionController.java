package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.indto.PageDto;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.pojo.Collection;
import com.service.CollectionService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/collection")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @GetMapping("/pageList")
    public String pageList(PageDto pageDto) {
        if (pageDto.getPageNum() < 0 || pageDto.getPageSize() <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        PageListDto pageListDto = collectionService.pageList(pageDto);
        OutputFormate outputFormate = new OutputFormate(pageListDto);
        return JSONObject.toJSONString(outputFormate);
    }

    @PostMapping("/add")
    public String add(@RequestBody Collection collection) {
        collectionService.add(collection);
        return ErrorCode.SUCCESS.toJsonString();
    }

    @PostMapping("/cancel")
    public String cancel(@RequestBody Collection collection) {
        collectionService.cancel(collection);
        return ErrorCode.SUCCESS.toJsonString();
    }
}
