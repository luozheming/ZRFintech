package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.indto.PageDto;
import com.dto.outdto.OutputFormate;
import com.pojo.Investor;
import com.pojo.Project;
import com.utils.ErrorCode;
import com.utils.NumGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
            return "参数错误";
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
            return "参数错误";
        } else {
            int startNum = pageNum * pageSize;
            List<Investor> investors = mongoTemplate.find(new Query().skip(startNum).limit(pageSize), Investor.class);
            OutputFormate outputFormate = new OutputFormate(investors);
            return JSONObject.toJSONString(outputFormate);
        }
    }

    /**
     *
     *  项目上传（表单）
     */
    @PostMapping(value = "projectfromupload")
    public String projectFormUpload(@RequestBody Project project){
        //需要编写项目代码生成器
        project.setProjectNo(numGenerate.getNumCode());
        mongoTemplate.save(project);
        return ErrorCode.SUCCESS.toJsonString();
    }



    /**
     * 项目上传（文件）
     */

    @RequestMapping(value = "uploadfile", produces = {"text/html;charset=UTF-8;"})
    public String upLoadFile(@RequestParam("file") MultipartFile file,@RequestParam("openId")String openId
            ,@RequestParam("projectNo")String projectNo) {
        if (!file.isEmpty()) {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 获取文件的后缀名
            //String suffixName = fileName.substring(fileName.lastIndexOf("."));
            // 文件上传后的路径
            StringBuilder filePathBuffer = new StringBuilder();
            String filePath = filePathBuffer.append("./data/bp/").append(openId).append("/").append(projectNo).append("/").toString();
            File dest = new File(filePath + fileName);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }try {
                file.transferTo(dest);
                //文件保存后更新数据库信息
                mongoTemplate.updateFirst(query(where("projectNo").is(projectNo)),new Update().set("bpRoute",filePath),Project.class);
            } catch (IllegalStateException e) {
                return ErrorCode.FILEUPLOADFAILED.toJsonString();
            } catch (IOException e) {
                return ErrorCode.FILEUPLOADFAILED.toJsonString();
            }
            return ErrorCode.SUCCESS.toJsonString();
        }else {
            return ErrorCode.EMPITYFILE.toJsonString();
        }
    }
}



