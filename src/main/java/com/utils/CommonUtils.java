package com.utils;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import org.bson.internal.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
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
    private String tempLocalFilePath;

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
     * 下载AWS S3的文件
     * @param bucketName
     * @param objectKey
     * @return
     */
    public File downloadS3File(String bucketName, String objectKey, String localFilePath) {
        File file = null;
        Region region = Region.CN_NORTHWEST_1;
        try {
            // Write the data to a local file
            file = new File(localFilePath);
            if (!file.isFile()) {
                file.createNewFile();
            }
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

            OutputStream os = new FileOutputStream(file);
            os.write(data);
            System.out.println("Successfully obtained bytes from an S3 object");
            os.close();
            // 清除临时文件
//            file.delete();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
        }
        return file;
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

    /**
     * 拼接文件全路径
     * @param filePath
     * @return
     */
    public String getRealFullFilePath(String filePath) {
        return "http://" + cdnDomainName + filePath;
    }

    public File downloadFileFromUrl(String urlPath) {
        File file = null;
        try {
            // 统一资源
            URL url = new URL(urlPath);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            //设置超时
            httpURLConnection.setConnectTimeout(1000*5);
            //设置请求方式，默认是GET
            httpURLConnection.setRequestMethod("POST");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 打开到此 URL引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.connect();
            // 文件大小
            int fileLength = httpURLConnection.getContentLength();

            // 控制台打印文件大小
            System.out.println("您要下载的文件大小为:" + fileLength / (1024 * 1024) + "MB");

            // 建立链接从请求中获取数据
//            URLConnection con = url.openConnection();
            BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());
            // 指定文件名称(有需求可以自定义)
            String fileFullName = urlPath.substring(urlPath.lastIndexOf("/") + 1, urlPath.length());
            // 指定存放位置(有需求可以自定义)
            String path = "d://data/temp" + "/" + fileFullName;
            file = new File(path);
            // 校验文件夹目录是否存在，不存在就创建一个目录
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            OutputStream out = new FileOutputStream(file);
            int size = 0;
            int len = 0;
            byte[] buf = new byte[2048];
            while ((size = bin.read(buf)) != -1) {
                len += size;
                out.write(buf, 0, size);
                // 控制台打印文件下载的百分比情况
                System.out.println("下载了-------> " + len * 100 / fileLength + "%\n");
            }
            // 关闭资源
            bin.close();
            out.close();
            System.out.println("文件下载成功！");
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("文件下载失败！");
        } finally {
            return file;
        }
    }
}
