package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.pojo.IntegralGoods;
import com.service.manage.IntegralGoodsService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/integralGoods")
public class IntegralGoodsController {

    @Autowired
    private IntegralGoodsService integralGoodsService;

    /**
     * 分页获取项目列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/pageList")
    public String pageList(Integer pageNum, Integer pageSize, BigDecimal integralStart, BigDecimal integralEnd) {
        if (pageNum < 0 || pageSize <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        try {
            int count = integralGoodsService.count();
            int totalPage = count/pageSize;
            PageListDto pageListDto = new PageListDto<IntegralGoods>();
            pageListDto.setTotal(count);
            if(pageNum <= totalPage){
                List<IntegralGoods> integralGoods =  integralGoodsService.pageList(pageNum, pageSize, integralStart, integralEnd);
                pageListDto.setList(integralGoods);
            }
            OutputFormate outputFormate = new OutputFormate(pageListDto);
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

}
