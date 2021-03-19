package com.controller;


import com.alibaba.fastjson.JSONObject;
import com.dto.indto.EntPaymentDto;
import com.dto.indto.GetCommentsDto;
import com.dto.outdto.OutputFormate;
import com.pojo.Project;
import com.pojo.ProjectComment;
import com.utils.ErrorCode;
import com.utils.NumGenerate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RestController
@RequestMapping("/investor")
public class InvestorController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private NumGenerate numGenerate;

    /**
     * 红包支付
     * @param entPaymentDtoList
     * @return
     */
    @PostMapping(value = "/entPayment")
    public String entPayment(@RequestBody List<EntPaymentDto> entPaymentDtoList){
        try{
            if (!CollectionUtils.isEmpty(entPaymentDtoList)) {
                List<ProjectComment> projectCommentList = new ArrayList<>();// 项目评论信息list
                ProjectComment projectComment = null;

                // 在项目编号下已有投资人id的list中累加
                List<String> expList = new ArrayList<>();
                for (EntPaymentDto entPaymentDto: entPaymentDtoList) {
                    expList.add(entPaymentDto.getInvestorId());

                    projectComment = new ProjectComment();
                    BeanUtils.copyProperties(entPaymentDto, projectComment);
                    projectComment.setId(numGenerate.getNumCode());// 评论主键ID
                    projectComment.setIsDone(false);// 评论完成标识：false-未评，true-已评
                    projectCommentList.add(projectComment);
                }
                // 更新项目expList
                Update update = new Update();
                update.addToSet("expList").each(expList);
                String projectNo = entPaymentDtoList.get(0).getProjectNo();// 项目编号
                mongoTemplate.updateFirst(query(where("projectNo").is(projectNo)), update, Project.class);

                // 批量初始化评论信息
                if (!CollectionUtils.isEmpty(projectCommentList)) {
                    mongoTemplate.insert(projectCommentList, ProjectComment.class);
                }
            }
            return ErrorCode.SUCCESS.toJsonString();
        }catch (Exception e){
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 分页获取评待论信息（投资人）
     * @param getCommentsDto
     * @return
     */
    @GetMapping(value = "/getCommentsByInvestorId")
    public String getCommentsByInvestorId(GetCommentsDto getCommentsDto){
        try{
            int pageNum = getCommentsDto.getPageNum();
            int pageSize = getCommentsDto.getPageSize();
            if (pageNum < 0 || pageSize <= 0 || StringUtils.isEmpty(getCommentsDto.getInvestorId())) {
                return ErrorCode.PAGEBELLOWZERO.toJsonString();
            } else {
                int startNum = pageNum * pageSize;
                Query query = new Query(Criteria.where("investorId").is(getCommentsDto.getInvestorId())
                        .and("isDone").is(getCommentsDto.getIsDone()))
                        .with(Sort.by(Sort.Order.asc("isDone")))
                        .with(Sort.by(Sort.Order.asc("updateTm")));
                List<ProjectComment> projectComments = mongoTemplate.find(query.skip(startNum).limit(pageSize), ProjectComment.class);
                OutputFormate outputFormate = new OutputFormate(projectComments, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
                return JSONObject.toJSONString(outputFormate);
            }
        }catch (Exception e){
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 分页获取评论信息（客户）
     * @param getCommentsDto
     * @return
     */
    @GetMapping(value = "/getCommentsByOpenId")
    public String getCommentsByOpenId(GetCommentsDto getCommentsDto){
        try{
            int pageNum = getCommentsDto.getPageNum();
            int pageSize = getCommentsDto.getPageSize();
            if (pageNum < 0 || pageSize <= 0 || StringUtils.isEmpty(getCommentsDto.getOpenId())) {
                return ErrorCode.PAGEBELLOWZERO.toJsonString();
            } else {
                int startNum = pageNum * pageSize;
                Criteria criteria = Criteria.where("openId").is(getCommentsDto.getOpenId());
                if (!StringUtils.isEmpty(getCommentsDto.getProjectNo())) {
                    criteria = criteria.and("projectNo").is(getCommentsDto.getProjectNo());
                }
                Query query = new Query(criteria).with(Sort.by(Sort.Order.asc("isDone"))).with(Sort.by(Sort.Order.asc("updateTm")));
                List<ProjectComment> projectComments = mongoTemplate.find(query.skip(startNum).limit(pageSize), ProjectComment.class);
                OutputFormate outputFormate = new OutputFormate(projectComments, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
                return JSONObject.toJSONString(outputFormate);
            }
        }catch (Exception e){
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 投资人保存评价，不更新isDone字段
     * @param projectComment
     * @return
     */
    @PostMapping(value = "/saveCommentByInvestor")
    public String saveCommentByInvestor(@RequestBody ProjectComment projectComment){
        try{
            Update update = new Update();
            update.set("favor", projectComment.getFavor());
            update.set("content", projectComment.getContent());
            update.set("updateTm", new Date());
            mongoTemplate.updateFirst(query(where("id").is(projectComment.getId())), update, ProjectComment.class);
            return ErrorCode.SUCCESS.toJsonString();
        }catch (Exception e){
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 投资人提交评价
     * @param projectComment
     * @return
     */
    @PostMapping(value = "/commitCommentByInvestor")
    public String commitCommentByInvestor(@RequestBody ProjectComment projectComment){
        try{
            Update update = new Update();
            update.set("isDone", true);
            update.set("favor", projectComment.getFavor());
            update.set("content", projectComment.getContent());
            update.set("updateTm", new Date());
            mongoTemplate.updateFirst(query(where("id").is(projectComment.getId())), update, ProjectComment.class);
            return ErrorCode.SUCCESS.toJsonString();
        }catch (Exception e){
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 客户提交回评
     * @param projectComment
     * @return
     */
    @PostMapping(value = "/commitCommentByEnt")
    public String commitCommentByEnt(@RequestBody ProjectComment projectComment){
        try{
            // 查询该评论信息
            ProjectComment projectCommentResp = mongoTemplate.findById(projectComment.getId(), ProjectComment.class);
            if (null == projectCommentResp) {
                return ErrorCode.NULLOBJECT.toJsonString();
            }
            // 项目未被点评则不允许回评
            if (projectCommentResp.getIsDone()) {
                return ErrorCode.FORBIDREPLY.toJsonString();
            }

            // 判断评分是否为空
            if (null == projectComment.getStars() || 0 == projectComment.getStars().intValue()) {
                return ErrorCode.NULLSTARS.toJsonString();
            }

            Update update = new Update();
            update.set("stars", projectComment.getStars());
            update.set("reply", projectComment.getReply());
            update.set("replyTm", new Date());
            Query query = new Query(Criteria.where("id").is(projectComment.getId()));
            mongoTemplate.updateFirst(query, update, ProjectComment.class);
            return ErrorCode.SUCCESS.toJsonString();
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 下载bp文件
     * @param response
     * @param projectNo
     * @return
     */
    @GetMapping(value = "/downLoadBP")
    public String downLoadBP(HttpServletResponse response, @RequestParam String projectNo) {
        try {
            // 获取bp文件路径
            String filePath = "";
            List<Project> projects = mongoTemplate.find(query(Criteria.where("projectNo").is(projectNo)).limit(1), Project.class);
            if (!CollectionUtils.isEmpty(projects)) {
                filePath = projects.get(0).getBpRoute();
            }

            // 下载文件
            return downLoadFile(response, filePath);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 文件下载
     * @param response
     * @param filePath
     * @return
     */
    @GetMapping(value = "/downLoadFile")
    public String downLoadFile(HttpServletResponse response, String filePath) {
        FileInputStream fis = null; //文件输入流
        BufferedInputStream bis = null;
        OutputStream os = null; //输出流
        try {
            File file =  new File(filePath);
            if (!file.exists()) {
                return ErrorCode.NULLOBJECT.toJsonString();
            }
            String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.length());

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

}
