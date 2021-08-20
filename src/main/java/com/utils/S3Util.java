package com.utils;

import com.alibaba.fastjson.JSONObject;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class S3Util {

    /**
     * AWS S3文件上传
     * @param bucketName
     * @param objectKey
     * @param fileData
     * @return
     */
    public static String uploadFile(String bucketName, String objectKey, byte[] fileData) {
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
            e.printStackTrace();
            System.out.println("系统异常：" + e);
            return "upload Exception";
        }
    }

    /**
     * AWS S3文件下载
     * @param bucketName
     * @param objectKey
     * @param response
     * @return
     */
    public static Boolean downLoadFile(String bucketName, String objectKey, HttpServletResponse response) {
        System.out.println("bucketName=" + bucketName);
        System.out.println("objectKey=" + objectKey);
        Region region = Region.CN_NORTHWEST_1;
        OutputStream out = null;
        try {
            if (objectKey.indexOf("/") == 0) {
                objectKey = objectKey.substring(1, objectKey.length());
            }
            S3Client s3Client = S3Client.builder().region(region).build();
            GetObjectRequest objectRequest = GetObjectRequest
                    .builder()
                    .key(objectKey)
                    .bucket(bucketName)
                    .build();
            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
            byte[] data = objectBytes.asByteArray();
            String fileName = objectKey.substring(objectKey.lastIndexOf("/") + 1, objectKey.length());
            System.out.println(data.length);
            // 将文件名称进行编码
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            out = response.getOutputStream();
            out.write(data);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("文件下载系统异常：" + e);
            return false;
        } finally {
            if (null != out) {
                try {
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

