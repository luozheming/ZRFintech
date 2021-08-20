package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.indto.PageDto;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.pojo.Attention;
import com.service.AttentionService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attention")
public class AttentionController {

    @Autowired
    private AttentionService attentionService;

    @GetMapping("/pageList")
    public String pageList(PageDto pageDto) {
        if (pageDto.getPageNum() < 0 || pageDto.getPageSize() <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        PageListDto pageListDto = attentionService.pageList(pageDto);
        OutputFormate outputFormate = new OutputFormate(pageListDto);
        return JSONObject.toJSONString(outputFormate);
    }

    @PostMapping("/add")
    public String add(@RequestBody Attention attention) {
        attentionService.add(attention);
        return ErrorCode.SUCCESS.toJsonString();
    }

    @PostMapping("/cancel")
    public String cancel(@RequestBody Attention attention) {
        attentionService.cancel(attention);
        return ErrorCode.SUCCESS.toJsonString();
    }
}
