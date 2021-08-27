package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.indto.OrderDto;
import com.dto.outdto.OrderOutDto;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.dto.outdto.WxPayDto;
import com.enums.OrderBizType;
import com.pojo.ActivityRecord;
import com.pojo.Order;
import com.pojo.Project;
import com.service.ActivityRecordService;
import com.service.OrderService;
import com.service.WxPayService;
import com.service.manage.ProjectCommentService;
import com.utils.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProjectCommentService projectCommentService;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @PostMapping("/createOrder")
    public String createOrder(@RequestBody OrderDto orderDto ) {
        WxPayDto wxPayDto = null;
        try {
           wxPayDto = orderService.createOrder(orderDto);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        OutputFormate outputFormate = new OutputFormate(wxPayDto, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    @PostMapping("/deleteOrder")
    public String deleteOrder(@RequestBody OrderDto orderDto) {
        // 获取订单信息
        Order order = orderService.detailByOrderNo(orderDto.getOrderNo());
        // 根据业务类型删除对应业务数据
        if (null != order) {
            OrderBizType orderBizType = OrderBizType.getInstance(order.getBizType());
            switch (orderBizType) {
                case PROJECTCOMMENT:
                    projectCommentService.delete(order.getBizId());
                    break;
                default:
                    break;
            }
            // 删除订单
            orderService.delete(order.getOrderNo());
        }

        return ErrorCode.SUCCESS.toJsonString();
    }

    @PostMapping("/status")
    public String status(String orderNo, Integer payStatus) {
        orderService.status(orderNo, payStatus);
        return ErrorCode.SUCCESS.toJsonString();
    }

    @PostMapping("/statusByBizId")
    public String statusByBizId(String bizId, Integer payStatus) {
        orderService.statusByBizId(bizId, payStatus);
        return ErrorCode.SUCCESS.toJsonString();
    }

    /**
     * 小程序展示的订单列表（不包含金卡会员购买订单）
     * @param pageNum
     * @param pageSize
     * @param openId
     * @param userId
     * @return
     */
    @GetMapping("/pageList")
    public String pageList(Integer pageNum, Integer pageSize, String openId, String userId) {
        logger.info("订单查询列表入参：userId=" +userId);
        if (pageNum < 0 || pageSize <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        try {
            int count = orderService.count(userId);
            int totalPage = count/pageSize;
            PageListDto pageListDto = new PageListDto<OrderOutDto>();
            pageListDto.setTotal(count);
            if(pageNum <= totalPage){
                List<OrderOutDto> orderOutDtos = orderService.pageList(pageNum, pageSize, openId, userId);
                pageListDto.setList(orderOutDtos);
            }
            OutputFormate outputFormate = new OutputFormate(pageListDto);
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 后台管理展示的订单列表（含金卡会员购买订单）
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    @GetMapping("/pageListAll")
    public String pageListAll(Integer pageNum, Integer pageSize, String userId) {
        if (pageNum < 0 || pageSize <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        try {
            int count = orderService.count(userId);
            int totalPage = count/pageSize;
            PageListDto pageListDto = new PageListDto<OrderOutDto>();
            pageListDto.setTotal(count);
            if(pageNum <= totalPage){
                List<OrderOutDto> orderOutDtos = orderService.pageListAll(pageNum, pageSize, userId);
                pageListDto.setList(orderOutDtos);
            }
            OutputFormate outputFormate = new OutputFormate(pageListDto);
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 提交评论
     * @param order
     * @return
     */
    @PostMapping("/commitReply")
    public String commitReply(@RequestBody Order order) {
        orderService.commitReply(order);
        return ErrorCode.SUCCESS.toJsonString();
    }

    /**
     * 查看订单
     * @param pageNum
     * @param pageSize
     * @param openId
     * @param userId
     * @return
     */
    @GetMapping("/pageOrderList")
    public String pageOrderList(Integer pageNum, Integer pageSize, String openId, String userId) {
        if (pageNum < 0 || pageSize <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        try {
            int count = orderService.count(userId);
            int totalPage = count/pageSize;
            PageListDto pageListDto = new PageListDto<OrderOutDto>();
            pageListDto.setTotal(count);
            if(pageNum <= totalPage){
                List<OrderOutDto> orderOutDtos = orderService.pageList(pageNum, pageSize, openId, userId);
                pageListDto.setList(orderOutDtos);
            }
            OutputFormate outputFormate = new OutputFormate(pageListDto);
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @PostMapping("/edit")
    public String edit(@RequestBody Order order) {
        orderService.update(order);
        return ErrorCode.SUCCESS.toJsonString();
    }

}
