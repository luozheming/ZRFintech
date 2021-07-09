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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private ProjectCommentService projectCommentService;
    @Autowired
    private ActivityRecordService activityRecordService;

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
//                case ACTIVITYRECORD:
//                    activityRecordService.delete(order.getBizId());
//                    break;
                default:
                    System.out.println("系统类型不满足");
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

    @GetMapping("/pageList")
    public String pageList(Integer pageNum, Integer pageSize, String openId, String userId) {
        if (pageNum < 0 || pageSize <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        }
        try {
            int count = orderService.count(openId);
            int totalPage = count/pageSize;
            PageListDto pageListDto = new PageListDto<Project>();
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
     * 提交评论
     * @param order
     * @return
     */
    @PostMapping("/commitReply")
    public String commitReply(@RequestBody Order order) {
        orderService.commitReply(order);
        return ErrorCode.SUCCESS.toJsonString();
    }
}
