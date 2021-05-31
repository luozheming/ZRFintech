package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.pojo.Order;
import com.service.OrderService;
import com.service.WxPayService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付相关接口
 */
@RestController
@RequestMapping("/wxPay")
public class WxPayController {

    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/notify")
    public String notify(HttpServletRequest request, HttpServletResponse response) {
        Map map = new HashMap<>();
        try {
            String requestData = readData(request);
            System.out.println("支付异步回调接口开始：requestData = " + requestData);
            // 需要通过证书序列号查找对应的证书，verifyNotify 中有验证证书的序列号
            String verifyData = wxPayService.verifyNotify(requestData);
            System.out.println("支付异步回调接口结束：verifyData = " + verifyData);
            if (!StringUtils.isEmpty(verifyData)) {
                Integer payStatus = 0;
                JSONObject jsonObject = JSONObject.parseObject(verifyData);
                String outTradeNo = jsonObject.getString("out_trade_no");
                String transactionId = jsonObject.getString("transaction_id");
                Order order = orderService.detailByOrderNo(outTradeNo);
                if ("SUCCESS".equals(jsonObject.getString("trade_state"))) {
                    payStatus = 1;
                    JSONObject amount = jsonObject.getJSONObject("amount");
                    BigDecimal total = new BigDecimal(amount.getString("total"));
                    if (total.divide(new BigDecimal("100")).compareTo(order.getPayAmount()) != 0) {
                        payStatus = 5;
                    }
                } else {
                    payStatus = 3;
                }
                order.setPayStatus(payStatus);
                order.setTransactionId(transactionId);
                orderService.update(order);

                response.setStatus(200);
                map.put("code", "SUCCESS");
                map.put("message", "SUCCESS");
            } else {
                response.setStatus(500);
                map.put("code", "ERROR");
                map.put("message", "签名错误");
            }
            response.setHeader("Content-type", "application/json; charset=utf-8");
            response.getOutputStream().write(JSONObject.toJSONString(map).getBytes(StandardCharsets.UTF_8));
            response.flushBuffer();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        return ErrorCode.SUCCESS.toJsonString();
    }

    /**
     * 处理返回对象
     *
     * @param request
     * @return
     */
    private String readData(HttpServletRequest request) {
        BufferedReader br = null;
        try {
            StringBuilder result = new StringBuilder();
            br = request.getReader();
            for (String line; (line = br.readLine()) != null; ) {
                if (result.length() > 0) {
                    result.append("\n");
                }
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
