package com.utils;

import com.alibaba.fastjson.JSONObject;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.dto.outdto.OutputFormate;
import org.bson.internal.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 编号生成器
 */
@Component
public class CommonUtils {

    @Value("${cdnDomainName}")
    private String cdnDomainName;

    public String getNumCode() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * 获取指定位数的随便数字
     * @param figure
     * @return
     */
    public Integer getIntCode(int figure) {
        int hashCode = UUID.randomUUID().toString().hashCode();
        if(hashCode < 0) {//有可能是负数
            hashCode = - hashCode;
        }
        if (String.valueOf(hashCode).length() > figure) {
            hashCode = Integer.valueOf(String.valueOf(hashCode).substring(0, figure));
        }
        return hashCode;
    }

    /**
     * 获取图片信息
     * @param filePath
     * @return
     */
    public String getPhoto(String filePath) {
        String photo = "";
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(filePath);
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int len = -1;
            while((len = fis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            byte[] fileByte = bos.toByteArray();
            //进行base64位加密
            photo = new String(Base64.encode(fileByte));
        } catch (Exception e) {
            return "";
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                }
                if (null != bos) {
                    bos.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return photo;
    }

    /**
     * 上传文件
     */
    public void uploadData(MultipartFile file,String savedfilepath) throws IllegalStateException,IOException{
        //文件上传可能会出问题
        if (null != file) {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 文件上传后的路径
            File dest = new File(savedfilepath + fileName);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(dest);
            } catch (IllegalStateException e) {
                throw e;
            } catch (IOException e) {
                throw e;
            }
        }
    }

    /**
     * 文件下载
     * @param response
     * @param filePath
     * @return
     */
    public String downLoadFile(HttpServletResponse response, String filePath) {
        FileInputStream fis = null; //文件输入流
        BufferedInputStream bis = null;
        OutputStream os = null; //输出流
        try {
            File file =  new File(filePath);
            if (!file.exists()) {
                return ErrorCode.NULLOBJECT.toJsonString();
            }
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());

            // 将文件名称进行编码
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

            byte[] buffer = new byte[1024];
            os = response.getOutputStream();
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            int i = bis.read(buffer);
            while(i != -1){
                os.write(buffer);
                i = bis.read(buffer);
            }

            OutputFormate outputFormate = new OutputFormate("", ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        } finally {
            try {
                if (null != bis) {
                    bis.close();
                }
                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                return ErrorCode.OTHEREEEOR.toJsonString();
            }
        }
    }

    /**
     * 上传文件至S3桶
     * @param bucketName
     * @param objectKey
     * @param fileData
     * @return
     */
    public String uploadFile(String bucketName, String objectKey, byte[] fileData) {
        Region region = Region.CN_NORTHWEST_1;
        try {
            if (objectKey.indexOf("/") == 0) {
                objectKey = objectKey.substring(1, objectKey.length());
            }
            S3Client s3Client = S3Client.builder().region(region).build();
            Map<String, String> metadata = new HashMap<>();
            metadata.put("x-amz-meta-myVal", "test");
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .metadata(metadata)
                    .build();
            PutObjectResponse response = s3Client.putObject(putOb, RequestBody.fromBytes(fileData));
            System.out.println(response.toString());
            return response.eTag();
        } catch (Exception e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            System.out.println("系统异常：" + e);
            return "upload Exception";
        }
    }

    /**
     * 拼接文件全路径
     * @param filePath
     * @return
     */
    public String getFullFilePath(String filePath) {
//        return "http://" + cdnDomainName + filePath;
        // "http://" + cdnDomainName 由前端拼接
        return filePath;
    }
}
