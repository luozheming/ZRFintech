package com.utils;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.util.HashMap;
import java.util.Map;

public class S3Util {

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
}

