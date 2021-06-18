package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.indto.contract.AccountRegisterDto;
import com.dto.indto.contract.ApplyCertDto;
import com.dto.indto.contract.GetCompanyVerifyUrlDto;
import com.dto.indto.contract.GetPersonVerifyUrlDto;
import com.dto.outdto.OutputFormate;
import com.fadada.sdk.client.FddClientBase;
import com.fadada.sdk.client.authForfadada.ApplyCert;
import com.fadada.sdk.client.authForfadada.GetCompanyVerifyUrl;
import com.fadada.sdk.client.authForfadada.GetPersonVerifyUrl;
import com.pojo.contract.ContractCustomer;
import com.utils.DateUtil;
import com.utils.ErrorCode;
import com.utils.SignUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RestController
@RequestMapping("/electronicContract")
public class ElectronicContractController {

    @Value("${fdd.appId}")
    private String appId;
    @Value("${fdd.appSecret}")
    private String appSecret;
    @Value("${fdd.hostUrl}")
    private String hostUrl;
    private final String version = "2.0";

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ElectronicContractController.class);

    /**
     * 注册账号
     * @param accountRegisterDto
     * @return
     */
    @PostMapping("/accountRegister")
    public String accountRegister(@RequestBody AccountRegisterDto accountRegisterDto) {
        String customerId = "";
        try {
            FddClientBase base = new FddClientBase(appId, appSecret, version, hostUrl);
            String result = base.invokeregisterAccount(accountRegisterDto.getOpen_id(), accountRegisterDto.getAccount_type());
            if (!StringUtils.isEmpty(result)) {
                String code = JSONObject.parseObject(result).getString("code");
                String msg = JSONObject.parseObject(result).getString("msg");
                customerId = JSONObject.parseObject(result).getString("data");
                if (!"1".equals(code)) {
                    OutputFormate outputFormate = new OutputFormate("", ErrorCode.OTHEREEEOR.getCode(), msg);
                    return JSONObject.toJSONString(outputFormate);
                }

                // 初始化用户注册账号信息
                ContractCustomer contractCustomer = new ContractCustomer();
                contractCustomer.setCustomerId(customerId);
                contractCustomer.setUserId(accountRegisterDto.getOpen_id());
                contractCustomer.setCreateTime(new Date());
                mongoTemplate.save(contractCustomer);
            }
        } catch (Exception e) {
            logger.error("系统错误", e);
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        OutputFormate outputFormate = new OutputFormate(customerId, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     * 获取企业实名认证地址
     * @param getCompanyVerifyUrlDto
     * @return
     */
    @PostMapping("/getCompanyVerifyUrl")
    public String getCompanyVerifyUrl(@RequestBody GetCompanyVerifyUrlDto getCompanyVerifyUrlDto) {
        try {
            GetCompanyVerifyUrl comverify = new GetCompanyVerifyUrl(appId, appSecret, version, hostUrl);
            String result = comverify.invokeCompanyVerifyUrl(null, null, null, null,
                    getCompanyVerifyUrlDto.getCustomer_id(), getCompanyVerifyUrlDto.getVerifyed_way(), null,
                    getCompanyVerifyUrlDto.getPage_modify(), getCompanyVerifyUrlDto.getCompany_principal_type(),
                    null,null, null, null, null,null,
                    null, null, null);
            if (!StringUtils.isEmpty(result)) {
                String code = JSONObject.parseObject(result).getString("code");
                String msg = JSONObject.parseObject(result).getString("msg");
                String data = JSONObject.parseObject(result).getString("data");
                if ("1".equals(code)) {
                    if (!StringUtils.isEmpty(data)) {
                        String transactionNo = JSONObject.parseObject(data).getString("transactionNo");
                        String url = new String(SignUtil.decodeBase64(JSONObject.parseObject(data).getString("url").getBytes(StandardCharsets.UTF_8)));
                        OutputFormate outputFormate = new OutputFormate(url, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());

                        // 更新交易号
                        Update update = new Update();
                        update.set("transactionNo", transactionNo);
                        update.set("url", url);
                        update.set("updateTime", new Date());
                        mongoTemplate.updateFirst(query(where("customerId").is(getCompanyVerifyUrlDto.getCustomer_id())), update, ContractCustomer.class);

                        return JSONObject.toJSONString(outputFormate);
                    }
                } else {
                    OutputFormate outputFormate = new OutputFormate(data, ErrorCode.OTHEREEEOR.getCode(), msg);
                    return JSONObject.toJSONString(outputFormate);
                }
            }
        } catch (Exception e) {
            logger.error("系统错误", e);
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        OutputFormate outputFormate = new OutputFormate("", ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     *  获取个人实名认证地址
     * @param getPersonVerifyUrlDto
     * @return
     */
    @PostMapping("/getPersonVerifyUrl")
    public String getPersonVerifyUrl(@RequestBody GetPersonVerifyUrlDto getPersonVerifyUrlDto) {
        try {
            GetPersonVerifyUrl personverify = new GetPersonVerifyUrl(appId, appSecret, version, hostUrl);
            String result = personverify.invokePersonVerifyUrl(getPersonVerifyUrlDto.getCustomer_id(),getPersonVerifyUrlDto.getVerifyed_way(),getPersonVerifyUrlDto.getPage_modify(),
                    getPersonVerifyUrlDto.getNotify_url(),getPersonVerifyUrlDto.getReturn_url(),getPersonVerifyUrlDto.getCustomer_name(),getPersonVerifyUrlDto.getCustomer_ident_type(),
                    getPersonVerifyUrlDto.getCustomer_ident_no(),getPersonVerifyUrlDto.getMobile(),getPersonVerifyUrlDto.getIdent_front_path(),getPersonVerifyUrlDto.getReturn_url(),
                    getPersonVerifyUrlDto.getCert_flag(),getPersonVerifyUrlDto.getCert_type(),getPersonVerifyUrlDto.getBank_card_no(),getPersonVerifyUrlDto.getOption(),getPersonVerifyUrlDto.getId_photo_optional(),getPersonVerifyUrlDto.getIs_mini_program());
            if (!StringUtils.isEmpty(result)) {
                String code = JSONObject.parseObject(result).getString("code");
                String msg = JSONObject.parseObject(result).getString("msg");
                String data = JSONObject.parseObject(result).getString("data");
                if ("1".equals(code)) {
                    if (!StringUtils.isEmpty(data)) {
                        String transactionNo = JSONObject.parseObject(data).getString("transactionNo");
                        String url = new String(SignUtil.decodeBase64(JSONObject.parseObject(data).getString("url").getBytes(StandardCharsets.UTF_8)));
                        OutputFormate outputFormate = new OutputFormate(url, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());

                        // 更新交易号
                        Update update = new Update();
                        update.set("transactionNo", transactionNo);
                        update.set("url", url);
                        update.set("updateTime", new Date());
                        mongoTemplate.updateFirst(query(where("customerId").is(getPersonVerifyUrlDto.getCustomer_id())), update, ContractCustomer.class);

                        return JSONObject.toJSONString(outputFormate);
                    }
                } else {
                    OutputFormate outputFormate = new OutputFormate(data, ErrorCode.OTHEREEEOR.getCode(), msg);
                    return JSONObject.toJSONString(outputFormate);
                }
            }
        } catch (Exception e) {
            logger.error("系统错误", e);
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        OutputFormate outputFormate = new OutputFormate("", ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     *  实名证书申请
     * @param applyCertDto
     * @return
     */
    @PostMapping("/applyCert")
    public String applyCert(@RequestBody ApplyCertDto applyCertDto) {
        try {
            ApplyCert applyCert = new ApplyCert(appId, appSecret, version, hostUrl);
            String result = applyCert.invokeApplyCert(applyCertDto.getCustomer_id(), applyCertDto.getVerified_serialno());
            if (!StringUtils.isEmpty(result)) {
                String code = JSONObject.parseObject(result).getString("code");
                String msg = JSONObject.parseObject(result).getString("msg");
                String data = JSONObject.parseObject(result).getString("data");
                if ("1".equals(code)) {
                    return ErrorCode.SUCCESS.toJsonString();
                } else {
                    OutputFormate outputFormate = new OutputFormate(data, ErrorCode.OTHEREEEOR.getCode(), msg);
                    return JSONObject.toJSONString(outputFormate);
                }
            }
        } catch (Exception e) {
            logger.error("系统错误", e);
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        OutputFormate outputFormate = new OutputFormate("", ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     * 印章上传
     * @param file
     * @param customer_id
     * @return
     */
    @PostMapping("/addSignature")
    public String addSignature(MultipartFile file, String customer_id) {
        try {
            File reqFile = new File(file.getOriginalFilename());
            file.transferTo(reqFile);
            FddClientBase base = new FddClientBase(appId, appSecret, version, hostUrl);
            String result = base.invokeaddSignature(customer_id, reqFile, null);
            if (!StringUtils.isEmpty(result)) {
                String code = JSONObject.parseObject(result).getString("code");
                String msg = JSONObject.parseObject(result).getString("msg");
                String data = JSONObject.parseObject(result).getString("data");
                if ("1".equals(code)) {
                    return ErrorCode.SUCCESS.toJsonString();
                } else {
                    OutputFormate outputFormate = new OutputFormate(data, ErrorCode.OTHEREEEOR.getCode(), msg);
                    return JSONObject.toJSONString(outputFormate);
                }
            }
        } catch (Exception e) {
            logger.error("系统错误", e);
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        OutputFormate outputFormate = new OutputFormate("", ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }








































    /**
     * 获取请求参数
     * @param bizParamMap
     * @param appId
     * @param appSecret
     * @return
     * @throws Exception
     */
    private Map<String, Object> getRequestParam(Map<String, Object> bizParamMap, String appId, String appSecret) throws Exception {
        bizParamMap = sortByKey(bizParamMap);
        String bizStr = "";
        for (Map.Entry<String, Object> entry : bizParamMap.entrySet()) {
            String mapValue = String.valueOf(entry.getValue());
            bizStr = bizStr + mapValue;
        }
        String timestamp = DateUtil.dateToStr(new Date(), "yyyyMMddHHmmss");
        String msgDigest = getMsgDigest(bizStr, appId, appSecret, timestamp);
        bizParamMap.put("app_id", appId);
        bizParamMap.put("timestamp", timestamp);
        bizParamMap.put("v", version);
        bizParamMap.put("msg_digest", msgDigest);
        return bizParamMap;
    }

    /**
     * 获取摘要
     * @param bizStr
     * @param appId
     * @param appSecret
     * @return
     */
    private String getMsgDigest(String bizStr, String appId, String appSecret, String timestamp) throws Exception {
        String secretStr = appSecret + bizStr;
        secretStr = SignUtil.getSha1Str(secretStr);
        String md5Str = SignUtil.getMD5Str(timestamp);
        String msgDigest = appId + md5Str + secretStr;
        msgDigest = SignUtil.encodeBase64(SignUtil.getSha1Str(msgDigest).getBytes());
        return msgDigest;
    }

    /**
     * map 按 key 升序排序
     */
    private Map<String, Object> sortByKey(Map<String, Object> map) {
        Map<String, Object> result = new LinkedHashMap<>(map.size());
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }


}
