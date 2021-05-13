package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.pojo.IntegralGoods;
import com.service.manage.IntegralGoodsService;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/integralGoods")
public class IntegralGoodsController {

    @Autowired
    private IntegralGoodsService integralGoodsService;
    @Autowired
    private CommonUtils commonUtils;
    @Value("${integralGoodsFilePath}")
    private String integralGoodsFilePath;

    /**
     * 分页获取积分商品列表
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

    /**
     * 查看详情
     * @param id
     * @return
     */
    @GetMapping("/detail")
    public String detail(@RequestParam String id) {
        IntegralGoods integralGoods = integralGoodsService.detail(id);
        if (null != integralGoods && !StringUtils.isEmpty(integralGoods.getPhotoRoute())) {
            integralGoods.setPhoto(commonUtils.getPhoto(integralGoods.getPhotoRoute()));
        }
        OutputFormate outputFormate = new OutputFormate(integralGoods, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     * 积分商品新增
     * @param photoFile
     * @param integralGoods
     * @return
     */
    @PostMapping("/add")
    public String add(MultipartFile photoFile, IntegralGoods integralGoods) {
        try {
            String filePath = integralGoodsFilePath;
            if (null != photoFile) {
                commonUtils.uploadData(photoFile, filePath);
                integralGoods.setPhotoRoute(filePath + "/" + photoFile.getOriginalFilename());
            }
            integralGoodsService.add(integralGoods);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        return ErrorCode.SUCCESS.toJsonString();
    }

}
