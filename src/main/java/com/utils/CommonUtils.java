package com.utils;

import org.bson.internal.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 编号生成器
 */
@Component
public class CommonUtils {


    public String getNumCode() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
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

}
