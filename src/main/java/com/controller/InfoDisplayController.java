package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.indto.PageDto;
import com.dto.indto.ProjectFormUploadDto;
import com.dto.outdto.OutputFormate;
import com.pojo.EntUser;
import com.pojo.Investor;
import com.pojo.Project;
import com.pojo.ProjectBpApply;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;


@RestController
public class InfoDisplayController {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CommonUtils commonUtils;

    @Value("${savedfilepath}")
    private String savedfilepath;

    /**
     * 项目展示（分页）
     * @param pageDto
     * @return
     */
    @PostMapping(value = "/investor/getProjectInfo")
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
    @PostMapping(value = "/entuser/getInvestorInfo")
    public String getInvestorInfo(@RequestBody PageDto pageDto) {
        int pageNum = pageDto.getPageNum();
        int pageSize = pageDto.getPageSize();
        //参数判断
        if (pageNum < 0 || pageSize <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        } else {
            int startNum = pageNum * pageSize;
            // 按isPlatform排序，将平台投资人置前
            List<Investor> investors = mongoTemplate.find(new Query(where("status").is(0)).with(Sort.by(Sort.Order.desc("isPlatform"))).skip(startNum).limit(pageSize), Investor.class);
            if (!CollectionUtils.isEmpty(investors)) {
                List<String> indusLabList = null;
                for (Investor investor : investors) {
                    indusLabList = new ArrayList<>();
                    investor.setPhoto(commonUtils.getPhoto(investor.getInvesPhotoRoute()));// 投资人头像
                    investor.setOrgPhoto(commonUtils.getPhoto(investor.getInvesOrgPhotoRoute()));// 投资人机构图片
                }
            }
            OutputFormate outputFormate = new OutputFormate(investors);
            return JSONObject.toJSONString(outputFormate);
        }
    }

    /**
     *  查询最新项目草稿
     */
    @PostMapping(value = "/project/getdraftbyid")
    public String projectFormUpload(@RequestBody ProjectFormUploadDto projectFormUploadDto){
        //查询项目草稿
        Project draftProject = mongoTemplate.findOne(query(where("openId").is(projectFormUploadDto.getOpenId()).and("isDone").is(false).and("projectType").is(projectFormUploadDto.getProjectType())),Project.class);
        OutputFormate outputFormate = new OutputFormate(draftProject);
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     * 项目上传（文件）
     */
    @PostMapping(value = "/project/uploadproject")
    public String upLoadProject(@RequestPart(value = "file", required = false) MultipartFile file, Project project) {
        String projectNo = (project.getIsDone() != null && project.getIsDone()) ? commonUtils.getNumCode() : null;// 生成主键ID
        //文件上传可能会出问题
        if (null != file) {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 获取文件的后缀名
            //String suffixName = fileName.substring(fileName.lastIndexOf("."));
            // 文件上传后的路径
            StringBuilder filePathBuffer = new StringBuilder();
            String filePath = filePathBuffer.append(savedfilepath).append(project.getOpenId()).append(File.separator).append(null == projectNo ? "temp" : projectNo).append(File.separator).toString();
            File dest = new File(filePath + fileName);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(dest);
                //文件保存后更新数据库信息
                project.setBpRoute(filePath + fileName);
            } catch (IllegalStateException e) {
                return ErrorCode.FILEUPLOADFAILED.toJsonString();
            } catch (IOException e) {
                return ErrorCode.FILEUPLOADFAILED.toJsonString();
            }
        }

        //如果是保存草稿，则更新草稿箱，并不分配项目id
        if (project.getIsDone() != null && !project.getIsDone()) {
            //查找并替换相应的草稿，如果草稿不存在，则进行插入操作
            mongoTemplate.update(Project.class)
                    .matching(query(where("openId").is(project.getOpenId()).and("isDone").is(false).and("projectType").is(project.getProjectType())))
                    .replaceWith(project)
                    .withOptions(FindAndReplaceOptions.options().upsert())
                    .findAndReplace();
            return ErrorCode.SUCCESS.toJsonString();
        } else {
            //需要编写项目代码生成器
            project.setProjectNo(projectNo);
            project.setCreateTime(new Date());
            // 项目类型：1-融资项目，2-路演项目，3-路演转融资
            if (2 == project.getProjectType()) {
                // 状态（路演项目）：1-审核中，2-等待审核结果，3-审核通过，4-暂未通过
                project.setStatus(1);
            }
            mongoTemplate.save(project);

            // 项目提交后删除对应的草稿项目
            mongoTemplate.remove(query(where("openId").is(project.getOpenId()).and("isDone").is(false).and("projectType").is(project.getProjectType())), Project.class);
            Project outputProject = Project.builder().projectNo(project.getProjectNo()).projectNm(project.getProjectNm()).build();
            OutputFormate outputFormate = new OutputFormate(outputProject);
            return JSONObject.toJSONString(outputFormate);
        }
    }

    /**
     * 项目bp申请
     * @param projectBpApply
     * @return
     */
    @PostMapping("/project/bpApply")
    public String bpApply(@RequestBody ProjectBpApply projectBpApply) {
        /*
        // 暂时删除vip卡逻辑代码
        // 1,扣除申请服务一次
        VIPCardUsage vipCardUsage = mongoTemplate.findOne(query(where("openId").is(projectBpApply.getOpenId())), VIPCardUsage.class);
        if (null == vipCardUsage) {
            return ErrorCode.VIPNOTPAYMENT.toJsonString();
        } else {
            if (vipCardUsage.getBpApplyTimes() -1 < 0) {
                return ErrorCode.VIPNOTENOUGH.toJsonString();
            }
            Integer bpApplyTimes = vipCardUsage.getBpApplyTimes() - 1;
            Update update = new Update();
            update.set("bpApplyTimes", bpApplyTimes);
            mongoTemplate.updateFirst(query(where("openId").is(projectBpApply.getOpenId())), update, VIPCardUsage.class);
        }*/
        // 通过项目编号查找项目
        Project project = mongoTemplate.findOne(query(where("projectNo").is(projectBpApply.getProjectNo())), Project.class);
        if (null != project) {
            BeanUtils.copyProperties(project, projectBpApply);
        }
        // 2,记录申请
        String id = commonUtils.getNumCode();// BP申请主键id
        projectBpApply.setId(id);
        projectBpApply.setCreateTime(new Date());
        projectBpApply.setStatus(0);
        mongoTemplate.save(projectBpApply);

        OutputFormate outputFormate = new OutputFormate(projectBpApply);
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     * 已上传项目查询
     */
    @GetMapping("/project/getMyProjects")
    public String getMyProjects(@RequestParam(value = "openId", required = true) String openId, String projectType){
        Query query = new Query();
        query.addCriteria(where("openId").is(openId));
        List<Integer> projectTypeList = new ArrayList<>();
        if (!StringUtils.isEmpty(projectType)) {
            for (String projectTypeStr : projectType.split(",")) {
                projectTypeList.add(Integer.valueOf(projectTypeStr));
            }
            query.addCriteria(where("projectType").in(projectTypeList));
            if (projectType.indexOf("1") == 1) {
                query.addCriteria(where("isDone").is(true));
            }
        }
        query.with(Sort.by(Sort.Order.asc("isDone")));
        List<Project> projectList = mongoTemplate.find(query, Project.class);
        OutputFormate outputFormate = new OutputFormate(projectList);
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     * 上传项目删除
     */
    @PostMapping("/project/deleteMyProject")
    public String deleteMyProject(@RequestParam(value = "projectNo", required = true) String projectNo){
        //删除项目列表中信息
        mongoTemplate.findAndRemove(query(where("projectNo").is(projectNo)),Project.class);
        return ErrorCode.SUCCESS.toJsonString();
    }

    /**
     * 记录浏览上传项目页面的动作
     * @param openId
     * @return
     */
    @PostMapping("/entuser/browseProjectPage")
    public String browseProjectPage(String openId, Integer projectType) {
        Update update = new Update();
        if (1 == projectType) {
            update.set("isBrowse", true);
        } else if (2 == projectType) {
            update.set("isRoadShowBrowse", true);
        }
        mongoTemplate.updateFirst(query(where("openId").is(openId)), update, EntUser.class);
        return ErrorCode.SUCCESS.toJsonString();
    }

}