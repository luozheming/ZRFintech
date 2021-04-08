package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.WxPayDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.service.WxPayService;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import com.utils.SignUtil;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Map;

@Service
public class WxPayServiceImpl implements WxPayService {

    @Value("appId")
    private String appId;
    @Value("merchantId")
    private String merchantId;
    @Value("privateKey")
    private String privateKey;
    @Value("merchantSerialNumber")
    private String merchantSerialNumber;
    @Value("apiV3Key")
    private String apiV3Key;

    @Autowired
    private CommonUtils commonUtils;

    @Override
    public WxPayDto wxPay(String openId, BigDecimal payAmount) throws Exception {

        // 加载商户私钥（privateKey：私钥字符串）
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(privateKey.getBytes("utf-8")));

        // 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3秘钥）
        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                new WechatPay2Credentials(merchantId, new PrivateKeySigner(merchantSerialNumber, merchantPrivateKey)),apiV3Key.getBytes("utf-8"));

        // 初始化httpClient
        HttpClient httpClient = WechatPayHttpClientBuilder.create()
                .withMerchant(merchantId, merchantSerialNumber, merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier)).build();

        // 请求URL
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type","application/json; charset=utf-8");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("mchid", merchantId)
                .put("appid", appId)
                .put("description", "微信红包支付")
                .put("out_trade_no", "")
                .put("notify_url", "");
        rootNode.putObject("amount")
                .put("total", payAmount);
        rootNode.putObject("payer")
                .put("openid", openId);
        objectMapper.writeValue(bos, rootNode);
        httpPost.setEntity(new StringEntity(bos.toString("UTF-8")));
        CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(httpPost);

        // 解析统一下单返回结果
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            throw new Exception(ErrorCode.PAYMENTEXCEPTION.getMessage());
        }
        String payResp = EntityUtils.toString(response.getEntity());

        // 组装JSAPI调起支付相关参数
        String timeStamp = String.valueOf(System.currentTimeMillis());// 时间戳
        String nonceStr = commonUtils.getNumCode();// 随机字符串
        String prepay_id = JSONObject.parseObject(payResp, Map.class).get("prepay_id").toString();
        String pendingPaySign = appId + "\n" + timeStamp + "\n" + nonceStr + "\n" + prepay_id + "\n";
        String paySign = SignUtil.encodeBase64(SignUtil.sign256(pendingPaySign, merchantPrivateKey));
        WxPayDto wxPayDto = new WxPayDto();
        wxPayDto.setAppId(appId);
        wxPayDto.setNonceStr(nonceStr);
        wxPayDto.setTimeStamp(timeStamp);
        wxPayDto.setPrepay_id(prepay_id);
        wxPayDto.setPaySign(paySign);
        wxPayDto.setSignType("RSA");

        return wxPayDto;
    }

    /**
     * v3 支付异步通知验证签名
     * @param requestData
     * @return 异步通知明文
     * @throws Exception
     */
    public String verifyNotify(String requestData) throws Exception {
        // 获取平台证书序列号
        JSONObject jsonObject = (JSONObject) JSONObject.parseObject(requestData, Map.class);
        String cipherText = jsonObject.getString("ciphertext");
        String nonceStr = jsonObject.getString("nonce");
        String associatedData = jsonObject.getString("associated_data");
        AesUtil aesUtil = new AesUtil(apiV3Key.getBytes(StandardCharsets.UTF_8));
        // 密文解密
        return aesUtil.decryptToString(
                associatedData.getBytes(StandardCharsets.UTF_8),
                nonceStr.getBytes(StandardCharsets.UTF_8),
                cipherText
        );
    }

}
