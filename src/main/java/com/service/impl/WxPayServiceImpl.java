package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.CertificateDto;
import com.dto.outdto.WxPayDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.service.WxPayService;
import com.utils.CertificateUtil;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import com.utils.SignUtil;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import okhttp3.HttpUrl;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Map;

@Service
public class WxPayServiceImpl implements WxPayService {

    @Value("${wx.appId}")
    private String appId;
    @Value("${wx.merchantId}")
    private String merchantId;
    @Value("${wx.apiV3Key}")
    private String apiV3Key = "qirongxing20210308qirongxing2021";
    @Value("${weChatPayCertFilePath}")
    private String weChatPayCertFilePath;

    @Autowired
    private CommonUtils commonUtils;

    @Override
    public WxPayDto wxPay(String orderNo, String openId, BigDecimal payAmount) throws Exception {

        // 加载商户私钥（privateKey：私钥字符串）
        CertificateDto certificateDto = CertificateUtil.createPKCS12(weChatPayCertFilePath, "tenpay certificate", merchantId);
        PrivateKey merchantPrivateKey = certificateDto.getPrivateKey();
        String merchantSerialNumber = certificateDto.getSerialNumber();

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
        httpPost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; .NET CLR 1.0.3705;)");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("mchid", merchantId)
                .put("appid", appId)
                .put("description", "企融星订单")
                .put("out_trade_no", orderNo)
                .put("notify_url", "https://zrfintech-dev.oa.cmbchina.biz/wxPay/notify");
        rootNode.putObject("amount")
                .put("total", payAmount);
        rootNode.putObject("payer")
                .put("openid", openId);
        objectMapper.writeValue(bos, rootNode);
        httpPost.setEntity(new StringEntity(bos.toString(), "utf-8"));
        CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(httpPost);

        // 解析统一下单返回结果
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            throw new Exception(ErrorCode.PAYMENTEXCEPTION.getMessage());
        }
        String payResp = EntityUtils.toString(response.getEntity());

        // 组装JSAPI调起支付相关参数
        String timeStamp = String.valueOf(System.currentTimeMillis());// 时间戳
        String nonceStr = commonUtils.getNumCode().substring(0, 30);// 随机字符串
        String prepay_id = JSONObject.parseObject(payResp, Map.class).get("prepay_id").toString();
        String packageStr = "prepay_id=" + prepay_id;
        String pendingPaySign = appId + "\n" + timeStamp + "\n" + nonceStr + "\n" + packageStr + "\n";
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
        System.out.println("支付异步回调接口进行中1");
        // 获取平台证书序列号
        JSONObject jsonObject = JSONObject.parseObject(requestData);
        JSONObject resource = (JSONObject) jsonObject.get("resource");
        String cipherText = resource.getString("ciphertext");
        String nonceStr = resource.getString("nonce");
        String associatedData = resource.getString("associated_data");
        AesUtil aesUtil = new AesUtil(apiV3Key.getBytes(StandardCharsets.UTF_8));
        System.out.println("支付异步回调接口进行中2");
        // 密文解密
        String str = aesUtil.decryptToString(
                associatedData.getBytes(StandardCharsets.UTF_8),
                nonceStr.getBytes(StandardCharsets.UTF_8),
                cipherText);
        return str;
    }

}
