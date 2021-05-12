package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.pojo.IntegralGoodsOrder;
import com.pojo.Investor;
import com.service.manage.IntegralGoodsOrderService;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/integralGoodsOrder")
public class IntegralGoodsOrderController {

    @Autowired
    private IntegralGoodsOrderService integralGoodsOrderService;
    @Autowired
    private CommonUtils commonUtils;

    /**
     * 积分兑换
     * @param integralGoodsOrder
     * @return
     */
    @PostMapping("/add")
    public String add(@RequestBody IntegralGoodsOrder integralGoodsOrder) {
        integralGoodsOrderService.add(integralGoodsOrder);
        return ErrorCode.SUCCESS.toJsonString();
    }

    /**
     * 分页查询积分商品订单信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/pageList")
    public String pageList(Integer pageNum, Integer pageSize) {
        if (pageNum < 0 || pageSize <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        try {
            int count = integralGoodsOrderService.count();
            int totalPage = count/pageSize;
            PageListDto pageListDto = new PageListDto<IntegralGoodsOrder>();
            pageListDto.setTotal(count);
            if(pageNum <= totalPage){
                List<IntegralGoodsOrder> integralGoodsOrders =  integralGoodsOrderService.pageList(pageNum, pageSize);
                if (!CollectionUtils.isEmpty(integralGoodsOrders)) {
                    for (IntegralGoodsOrder integralGoodsOrder : integralGoodsOrders) {
                        if (!StringUtils.isEmpty(integralGoodsOrder.getPhotoRoute())) {
                            integralGoodsOrder.setPhoto(commonUtils.getPhoto(integralGoodsOrder.getPhotoRoute()));
                        }
                    }
                }
                pageListDto.setList(integralGoodsOrders);
            }
            OutputFormate outputFormate = new OutputFormate(pageListDto);
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 更新积分商品兑换订单处理状态
     * @param id
     * @param dealStatus
     * @return
     */
    @PostMapping("/dealStatus")
    public String dealStatus(String id, Integer dealStatus) {
        integralGoodsOrderService.dealStatus(id, dealStatus);
        return ErrorCode.SUCCESS.toJsonString();
    }

    /**
     * 通过userId查询投资人信息
     * @param userId
     * @return
     */
    @GetMapping("/investorByUserId")
    public String investorByUserId(String userId) {
        Investor investor = integralGoodsOrderService.investorById(userId);
        OutputFormate outputFormate = new OutputFormate(investor, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }
}
