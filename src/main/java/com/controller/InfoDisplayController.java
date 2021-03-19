package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.indto.PageDto;
import com.dto.outdto.OutputFormate;
import com.pojo.Investor;
import com.pojo.Project;
import com.utils.ErrorCode;
import com.utils.NumGenerate;
import org.bson.internal.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;


@RestController
public class InfoDisplayController {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private NumGenerate numGenerate;

    /**
     * 项目展示（分页）
     * @param pageDto
     * @return
     */
    @PostMapping(value = "getProjectInfo")
    public String getProjectInfo(@RequestBody PageDto pageDto) {
        int pageNum = pageDto.getPageNum();
        int pageSize = pageDto.getPageSize();
        //参数判断
        if (pageNum < 0 || pageSize <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        } else {
            int startNum = pageNum * pageSize;
            List<Project> projects = mongoTemplate.find(new Query().skip(startNum).limit(pageSize), Project.class);
            OutputFormate outputFormate = new OutputFormate(projects);
            return JSONObject.toJSONString(outputFormate);
        }
    }

    /**
     * 投资人信息展示（分页）
     * @param pageDto
     * @return
     */
    @PostMapping(value = "getInvestorInfo")
    public String getInvestorInfo(@RequestBody PageDto pageDto) {
        int pageNum = pageDto.getPageNum();
        int pageSize = pageDto.getPageSize();
        //参数判断
        if (pageNum < 0 || pageSize <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        } else {
            int startNum = pageNum * pageSize;
            List<Investor> investors = mongoTemplate.find(new Query().skip(startNum).limit(pageSize), Investor.class);
            if (!CollectionUtils.isEmpty(investors)) {
                for (Investor investor : investors) {
                    investor.setPhoto(getPhoto(investor.getInvesPhotoRoute()));// 投资人头像
                    investor.setOrgPhoto(getPhoto(investor.getInvesOrgPhotoRoute()));// 投资人机构图片
                }
            }
            OutputFormate outputFormate = new OutputFormate(investors);
            return JSONObject.toJSONString(outputFormate);
        }
    }

    /**
     * 获取图片信息
     * @param filePath
     * @return
     */
    public String getPhoto(String filePath) {
        String photo = "";
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream bos = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int len = -1;
            while((len = fileInputStream.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            byte[] fileByte = bos.toByteArray();
            //进行base64位加密
            photo = new String(Base64.encode(fileByte));
        } catch (Exception e) {
            return "";
        } finally {
            try {
                if (null != fileInputStream) {
                    fileInputStream.close();
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
     *
     *  项目上传（表单）
     */
//    @PostMapping(value = "projectfromupload")
//    public String projectFormUpload(@RequestBody Project project){
//        //需要编写项目代码生成器
//        project.setProjectNo(numGenerate.getNumCode());
//        mongoTemplate.save(project);
//        //仅仅返回两个字段
//        Project outputProject = Project.builder().projectNo(project.getProjectNo()).projectNm(project.getProjectNm()).build();
//        OutputFormate outputFormate = new OutputFormate(outputProject);
//        return JSONObject.toJSONString(outputFormate);
//    }



    /**
     * 项目上传（文件）
     */
    @PostMapping(value = "uploadproject")
    public String upLoadProject(@RequestParam("file") MultipartFile file,@RequestBody Project project) {
        //需要编写项目代码生成器
        project.setProjectNo(numGenerate.getNumCode());
        mongoTemplate.save(project);
        //文件上传可能会出问题，但是文件
        if (!file.isEmpty()) {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 获取文件的后缀名
            //String suffixName = fileName.substring(fileName.lastIndexOf("."));
            // 文件上传后的路径
            StringBuilder filePathBuffer = new StringBuilder();
            String filePath = filePathBuffer.append("D:\\").append(project.getOpenId()).append("\\").append(project.getProjectNo()).append("\\").toString();
            File dest = new File(filePath + fileName);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }try {
                file.transferTo(dest);
                //文件保存后更新数据库信息
                project.setBpRoute(filePath);
            } catch (IllegalStateException e) {
                return ErrorCode.FILEUPLOADFAILED.toJsonString();
            } catch (IOException e) {
                return ErrorCode.FILEUPLOADFAILED.toJsonString();
            }
        }
            Project outputProject = Project.builder().projectNo(project.getProjectNo()).projectNm(project.getProjectNm()).build();
            OutputFormate outputFormate = new OutputFormate(outputProject);
            return JSONObject.toJSONString(outputFormate);
    }
}



