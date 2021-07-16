package com.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dto.indto.EntUserEditDto;
import com.dto.indto.UserRegisterDto;
import com.dto.indto.UserUpdatePasswordDto;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.UserLoginDto;
import com.pojo.*;
import com.service.UserLoginService;
import com.service.VIPCardUsageService;
import com.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RestController
public class UserLoginController {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private VIPCardUsageService vipCardUsageService;
    @Value("${wx.appId}")
    private String appId;
    @Value("${wx.secret}")
    private String secret;
    @Value("${wx.auth.code2Session}")
    private String code2SessionUrl;
    @Value("${entUserSavedFilepath}")
    StringBuilder entUserSavedFilepath;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private CommonUtils commonUtils;
    @Value("${s3BucketName}")
    private String s3BucketName;

    @PostMapping(value = "/investor/investorLogin")
    public String investorLogin(@RequestBody Investor investor){
        try{
            String phoneNm = investor.getPhoneNm();
            Investor findInvestor = mongoTemplate.findOne(query(where("phoneNm").is(phoneNm).and("status").is(0)),Investor.class);
            if(!StringUtils.isEmpty(findInvestor)){
                OutputFormate outputFormate = new OutputFormate(findInvestor);
                return JSONObject.toJSONString(outputFormate);
            }else{
                return ErrorCode.NULLOBJECT.toJsonString();
            }
        }catch (Exception e){
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @PostMapping(value = "/entuser/entUserLogin")
    public String entUserLogin(@RequestBody EntUser entUser){
        try{
            //如果用户已存在数据库，返回成功信息。否则将用户数据保存至数据库
            EntUser entUserResp = mongoTemplate.findOne(query(where("openId").is(entUser.getOpenId())),EntUser.class);
            if(!StringUtils.isEmpty(entUserResp)){
                // 如果已入库的客户信息没有客户id则更新一个，该分支用于处理一期历史数据
                if (StringUtils.isEmpty(entUserResp.getEntUserId())) {
                    Update update = new Update();
                    String entUserId = commonUtils.getNumCode();
                    update.set("entUserId", entUserId);
                    mongoTemplate.updateFirst(query(where("openId").is(entUserResp.getOpenId())), update, EntUser.class);
                    entUserResp.setEntUserId(entUserId);

                    // 通过openId更新项目表Project的entUserId，方便后续查询时跟PC端同步用entUserId查询相关信息
                    Update projectUpdate = new Update();
                    projectUpdate.set("entUserId", entUserId);
                    mongoTemplate.updateMulti(query(where("openId").is(entUser.getOpenId())), update, Project.class);

                    // 通过openId更新评论表ProjectComment的entUserId，方便后续查询时跟PC端同步用entUserId查询相关信息
                    Update commentUpdate = new Update();
                    commentUpdate.set("entUserId", entUserId);
                    mongoTemplate.updateMulti(query(where("openId").is(entUser.getOpenId())), update, ProjectComment.class);
                }
                OutputFormate outputFormate = new OutputFormate(entUserResp, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
                return JSONObject.toJSONString(outputFormate);
            }else{
                String entUserId = commonUtils.getNumCode();
                // 通过手机号码获取基础用户信息
                User user = mongoTemplate.findOne(query(where("phoneNm").is(entUser.getPhoneNm()).and("status").is(0)), User.class);
                if (null != user) {
                    entUserId = user.getUserId();
                }
                entUser.setEntUserId(entUserId);// 客户id同用户基础表userId
                entUser.setIsVerify(false);
                entUser.setCreateTime(new Date());
                mongoTemplate.insert(entUser);
                OutputFormate outputFormate = new OutputFormate(entUser, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
                return JSONObject.toJSONString(outputFormate);
            }
        }catch (Exception e){
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 微信登录
     * @param js_code
     * @return
     */
    @GetMapping("/wx/getCode2Session")
    public String getCode2Session(@RequestParam(value = "js_code", required = true) String js_code) {
        try {
            String url = code2SessionUrl + "?appId=" + appId + "&secret=" + secret
                    + "&js_code=" + js_code + "&grant_type=authorization_code";
            String resp = HttpClientUtil.doGet(url);
            OutputFormate outputFormate = new OutputFormate(resp, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 客户标记需要联系客服
     * @param openId
     * @return
     */
    @PostMapping("/entuser/contactService")
    public String contactService(String openId, String phoneNm) {
        Update update = new Update();
        update.set("isContactService", true);
        if (!StringUtils.isEmpty(openId)) {
            mongoTemplate.updateFirst(query(where("openId").is(openId)), update, EntUser.class);
        } else {
            mongoTemplate.updateFirst(query(where("phoneNm").is(phoneNm)), update, EntUser.class);
        }

        return ErrorCode.SUCCESS.toJsonString();
    }

    /**
     * 修改客户信息
     * @param entUser
     * @return
     */
    @PostMapping("/entuser/edit")
    public String edit(EntUser entUser) {
        Update update = new Update();
        update.set("companyName", entUser.getCompanyName());
        update.set("positionName", entUser.getPositionName());
        mongoTemplate.updateFirst(query(where("entUserId").is(entUser.getEntUserId())), update, EntUser.class);
        return ErrorCode.SUCCESS.toJsonString();
    }

    /**
     * 企业客户认证
     * @param cardFile
     * @param entUser
     * @return
     */
    @PostMapping("/entuser/verify")
    public String verify(MultipartFile cardFile, EntUser entUser, String captcha) {
        Map<String, Object> respMap = userLoginService.validateSms(entUser.getPhoneNm(), captcha);
        if (!(boolean)respMap.get("result")) {
            OutputFormate outputFormate = new OutputFormate("", ErrorCode.OTHEREEEOR.getCode(), String.valueOf(respMap.get("msg")));
            return JSONObject.toJSONString(outputFormate);
        }
        try {
            Update update = new Update();
            if (null != cardFile) {
                String filePath = entUserSavedFilepath + entUser.getEntUserId() + "/" + cardFile.getOriginalFilename();
                // AWS S3存储文件
                commonUtils.uploadFile(s3BucketName, filePath, cardFile.getBytes());
                //文件保存后更新数据库信息
                update.set("cardRoute", filePath);
            }
            update.set("userName", entUser.getUserName());
            update.set("companyName", entUser.getCompanyName());
            update.set("positionName", entUser.getPositionName());
            update.set("phoneNm", entUser.getPhoneNm());
            update.set("email", entUser.getEmail());
            update.set("isVerify", true);
            mongoTemplate.updateFirst(query(where("entUserId").is(entUser.getEntUserId())), update, EntUser.class);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }


//    ---------------------------------项目一期PC版部分接口调整----------------------------------------------------

    /**
     * 用户注册
     * @param userRegisterDto
     * @return
     */
    @PostMapping("/user/register")
    public String register(@RequestBody UserRegisterDto userRegisterDto) {
        try {
            userLoginService.register(userRegisterDto);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            OutputFormate outputFormate = new OutputFormate(null, ErrorCode.OTHEREEEOR.getCode(), e.getMessage());
            return JSONObject.toJSONString(outputFormate);
        }
    }

    /**
     * 用户密码登录
     * @param user
     * @return
     */
    @PostMapping("/user/loginByPassword")
    public String loginByPassword(@RequestBody User user) {
        try {
            UserLoginDto userLoginDto = userLoginService.loginByPassword(user);
            if (null == userLoginDto) {
                return ErrorCode.EMPITYUSER.toJsonString();
            }
            OutputFormate outputFormate = new OutputFormate(userLoginDto, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            OutputFormate outputFormate = new OutputFormate(null, ErrorCode.OTHEREEEOR.getCode(), e.getMessage());
            return JSONObject.toJSONString(outputFormate);
        }
    }

    /**
     * 用户修改密码
     * @param userUpdatePasswordDto
     * @return
     */
    @PostMapping("/user/updatePassword")
    public String updatePassword(@RequestBody UserUpdatePasswordDto userUpdatePasswordDto) {
        try {
            userLoginService.updatePassword(userUpdatePasswordDto);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            OutputFormate outputFormate = new OutputFormate(null, ErrorCode.OTHEREEEOR.getCode(), e.getMessage());
            return JSONObject.toJSONString(outputFormate);
        }
    }

    /**
     * 修改用户
     * @param photoFile
     * @param entUser
     * @return
     */
    @PostMapping("/user/edit")
    public String edit(MultipartFile photoFile, EntUser entUser) {
        try {
            String destPhotoPath = entUserSavedFilepath.toString();
            if (null != photoFile) {
                // AWS S3存储文件
                commonUtils.uploadFile(s3BucketName,destPhotoPath + photoFile.getOriginalFilename(), photoFile.getBytes());
                entUser.setPhotoRoute(destPhotoPath + photoFile.getOriginalFilename());
            }
            userLoginService.edit(entUser);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 查询用户详情
     * @param userId
     * @return
     */
    @GetMapping("/user/detail")
    public String detail(@RequestParam String userId) {
        EntUser entUser = userLoginService.detail(userId);
        OutputFormate outputFormate = new OutputFormate(entUser, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     * 通过userId查询投资人信息
     * @param userId
     * @return
     */
    @GetMapping("/investor/investorByUserId")
    public String investorByUserId(String userId) {
        Investor investor = userLoginService.investorById(userId);
        OutputFormate outputFormate = new OutputFormate(investor, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     * 发送短信验证码
     * @param smsType
     * @param phoneNm
     * @return
     */
    @PostMapping("/entUser/sendSms")
    public String sendSms(Integer smsType, String phoneNm) {
        try {
            userLoginService.sendSms(smsType, phoneNm);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 获取s3路径
     * @param bucketName,objectKey
     * @return
     */
    @GetMapping("/getUrl")
    public String getUrl(String bucketName, String objectKey) {
        try {
            String url = GeneratePresignedURLUtil.GeneratePresignedURL(bucketName, objectKey);
            OutputFormate outputFormate = new OutputFormate(url, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     *
     * @param file
     * @param bucketName
     * @param objectKey
     * @return
     */
    @PostMapping("/s3/uploadFile")
    public String uploadFile(MultipartFile file, String bucketName, String objectKey) {
        try {
            byte[] fileData = file.getBytes();
            String str = S3Util.uploadFile(bucketName, objectKey, fileData);
            return JSONObject.toJSONString(str);
        } catch (Exception e) {
            e.getMessage();
            return e.getMessage();
        }
    }




//    ----------------------公众号测试代码--------------------
    private static String getToken() throws Exception {
        // access_token接口https请求方式: GET https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
        String path = " https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
        String appid = "wx721ab3d889e118c7";
        String secret = "dae62109f0aa9d6501ab5469ab6a4bc4";
        URL url = new URL(path+"&appid=" + appid + "&secret=" + secret);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        InputStream in = connection.getInputStream();
        byte[] b = new byte[100];
        int len = -1;
        StringBuffer sb = new StringBuffer();
        while((len = in.read(b)) != -1) {
            sb.append(new String(b,0,len));
        }

        System.out.println(sb.toString());
        in.close();
        return sb.toString();
    }

    private static String getContentList(String token) throws IOException {
        String path = " https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=" + token;
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("content-type", "application/json;charset=utf-8");
        connection.connect();
        // post发送的参数
        Map<String, Object> map = new HashMap<>();
        map.put("type", "news"); // news表示图文类型的素材，具体看API文档
        map.put("offset", 0);
        map.put("count", 1);
        // 将map转换成json字符串
        String paramBody = JSON.toJSONString(map); // 这里用了Alibaba的fastjson

        OutputStream out = connection.getOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
        bw.write(paramBody); // 向流中写入参数字符串
        bw.flush();

        InputStream in = connection.getInputStream();
        byte[] b = new byte[100];
        int len = -1;
        StringBuffer sb = new StringBuffer();
        while((len = in.read(b)) != -1) {
            sb.append(new String(b,0,len));
        }

        in.close();
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {

        String result1 = getToken();
        Map<String,Object> token = (Map<String, Object>) JSON.parseObject(result1);
        String result2 = getContentList(token.get("access_token").toString());
        System.out.println(result2);
    }

}
