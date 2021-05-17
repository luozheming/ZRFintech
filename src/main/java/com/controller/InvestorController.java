package com.controller;


import com.alibaba.fastjson.JSONObject;
import com.dto.indto.EntPaymentDto;
import com.dto.indto.GetCommentsDto;
import com.dto.outdto.CommentProjectDto;
import com.dto.outdto.InvestorCommentAmountDto;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.ProjectCommentDetailDto;
import com.pojo.*;
import com.service.MessageService;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RestController
public class InvestorController {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CommonUtils commonUtils;
    @Value("${commentFilePath}")
    private String commentFilePath;
    @Autowired
    private MessageService messageService;

    /**
     * 红包支付
     * @param entPaymentDtoList
     * @return
     */
    @PostMapping(value = "/investor/entPayment")
    public String entPayment(@RequestBody List<EntPaymentDto> entPaymentDtoList){
        try{
            if (!CollectionUtils.isEmpty(entPaymentDtoList)) {
                String openId = entPaymentDtoList.get(0).getOpenId();

                Integer commentCount = 0;// 评论服务次数
                List<ProjectComment> projectCommentList = new ArrayList<>();// 项目评论信息list
                ProjectComment projectComment = null;

                // 在项目编号下已有投资人id的list中累加
                List<String> expList = new ArrayList<>();
                for (EntPaymentDto entPaymentDto: entPaymentDtoList) {
                    // 非平台投资人需要累积扣除服务次数
                    if (!entPaymentDto.getIsPlatform()) {
                        commentCount++;
                    }

                    expList.add(entPaymentDto.getInvestorId());
                    projectComment = new ProjectComment();
                    BeanUtils.copyProperties(entPaymentDto, projectComment);
                    projectComment.setId(commonUtils.getNumCode());// 评论主键ID
                    projectComment.setIsDone(false);// 评论完成标识：false-未评，true-已评
                    projectComment.setFavor(2);// 重点关注:1-感兴趣，2-未标记，3-不感兴趣，4-拒绝
                    projectComment.setCommentType(1);// 评论类型：1-点评，2-追问
                    projectComment.setCreateTime(new Date());
                    projectCommentList.add(projectComment);
                }
/*
                // 先扣除用户的vip卡的服务使用次数
                Integer commentTimes = 0;
                VIPCardUsage vipCardUsage = mongoTemplate.findOne(query(where("openId").is(openId)), VIPCardUsage.class);
                if (null != vipCardUsage && commentCount > 0) {
                    if (commentCount > vipCardUsage.getCommentTimes()) {
                        return ErrorCode.VIPNOTENOUGH.toJsonString();
                    } else {
                        commentTimes = vipCardUsage.getCommentTimes() - commentCount;
                    }
                    Update usageUpdate = new Update();
                    usageUpdate.set("commentTimes", commentTimes);
                    mongoTemplate.updateFirst(query(where("openId").is(openId)), usageUpdate, VIPCardUsage.class);
                } else if (null == vipCardUsage && commentCount > 0) {
                    return ErrorCode.VIPNOTPAYMENT.toJsonString();
                }*/

                // 更新项目expList
                Update update = new Update();
                update.addToSet("expList").each(expList);
                update.set("isPay", true);
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
    @GetMapping(value = "/investor/getCommentsByInvestorId")
    public String getCommentsByInvestorId(GetCommentsDto getCommentsDto){
        try{
            int pageNum = getCommentsDto.getPageNum();
            int pageSize = getCommentsDto.getPageSize();
            if (pageNum < 0 || pageSize <= 0 || StringUtils.isEmpty(getCommentsDto.getInvestorId())) {
                return ErrorCode.PAGEBELLOWZERO.toJsonString();
            } else {
                int startNum = pageNum * pageSize;
                Query query = new Query(Criteria.where("investorId").is(getCommentsDto.getInvestorId())
                        .and("isDone").is(getCommentsDto.getIsDone()).and("favor").ne(4))
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
    @GetMapping(value = "/entuser/getCommentsByOpenId")
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
                Query query = new Query(criteria).with(Sort.by(Sort.Order.desc("isDone")))
                        .with(Sort.by(Sort.Order.asc("favor")))
                        .with(Sort.by(Sort.Order.asc("updateTm")));
                List<ProjectComment> projectComments = mongoTemplate.find(query.skip(startNum).limit(pageSize), ProjectComment.class);
                if (!CollectionUtils.isEmpty(projectComments)) {
                    for (ProjectComment projectComment : projectComments) {
                        projectComment.setPhoto(commonUtils.getPhoto(projectComment.getInvesPhotoRoute()));
                    }
                }
                OutputFormate outputFormate = new OutputFormate(projectComments, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
                return JSONObject.toJSONString(outputFormate);
            }
        }catch (Exception e){
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 保存评价
     * @param projectComment
     * @return
     */
    @PostMapping(value = "/investor/saveCommentByInvestor")
    public String saveCommentByInvestor(@RequestBody ProjectComment projectComment){
        try{
            Update update = new Update();
            if (!StringUtils.isEmpty(projectComment.getFavor())) {
                update.set("favor", projectComment.getFavor());
            }
            if (!StringUtils.isEmpty(projectComment.getContent())) {
                update.set("content", projectComment.getContent());
                update.set("updateTm", new Date());
            }
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
    @PostMapping(value = "/investor/commitCommentByInvestor")
    public String commitCommentByInvestor(@RequestBody ProjectComment projectComment){
        try{
            // 评论字数不得少于200字
            if (StringUtils.isEmpty(projectComment.getContent()) || (!StringUtils.isEmpty(projectComment.getContent()) && projectComment.getContent().length() < 200)) {
                return ErrorCode.CONTENTLESS.toJsonString();
            }
            Update update = new Update();
            update.set("isDone", true);
            if (null != projectComment.getFavor()) {
                update.set("favor", projectComment.getFavor());
            }
            update.set("content", projectComment.getContent());
            update.set("updateTm", new Date());
            mongoTemplate.updateFirst(query(where("id").is(projectComment.getId())), update, ProjectComment.class);

            // 生成消息推送数据
            String msg = "";
            if (1 == projectComment.getCommentType()) {
                msg = "您有一条投资人[" + projectComment.getInvestor() + "]反馈待接收";
            } else if (2 == projectComment.getCommentType()) {
                msg = "您有一条追问投资人[" + projectComment.getInvestor() + "]已回复";
            }
            Message message = new Message();
            message.setId(commonUtils.getNumCode());
            message.setMsg(msg);
            message.setSender("系统消息");
            message.setCreateTime(new Date());
            message.setUserId(projectComment.getEntUserId());
            message.setStatus(0);
            messageService.add(message);
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
    @PostMapping(value = "/entuser/commitCommentByEnt")
    public String commitCommentByEnt(@RequestBody ProjectComment projectComment){
        try{
            // 查询该评论信息
            ProjectComment projectCommentResp = mongoTemplate.findById(projectComment.getId(), ProjectComment.class);
            if (null == projectCommentResp) {
                return ErrorCode.NULLOBJECT.toJsonString();
            }
            // 项目未被点评则不允许回评
            if (!projectCommentResp.getIsDone()) {
                return ErrorCode.FORBIDREPLY.toJsonString();
            }

            // 判断评分是否为空
            if (null == projectComment.getStars()) {
                return ErrorCode.NULLSTARS.toJsonString();
            }

            Update update = new Update();
            update.set("stars", projectComment.getStars());
            update.set("reply", projectComment.getReply());
            update.set("replyTm", new Date());
            Query query = new Query(Criteria.where("id").is(projectComment.getId()));
            mongoTemplate.updateFirst(query, update, ProjectComment.class);

            // 通过手机号码获取投资人用户信息
            Investor investor = mongoTemplate.findOne(query(where("investorId").is(projectComment.getInvestorId())), Investor.class);
            String userId = "";
            if (null != investor) {
                EntUser entUser = mongoTemplate.findOne(query(where("phoneNm").is(investor.getPhoneNm())), EntUser.class);
                userId = entUser.getUserId();
            }

            // 生成消息推送数据
            Message message = new Message();
            message.setId(commonUtils.getNumCode());
            message.setMsg("您有一条项目[" + projectComment.getProjectNm() + "]客户回评待接收");
            message.setSender("系统消息");
            message.setCreateTime(new Date());
            message.setUserId(userId);
            message.setStatus(0);
            messageService.add(message);
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
    @GetMapping(value = "/project/downLoadBP")
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
    public String downLoadFile(HttpServletResponse response, @RequestParam String filePath) {
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

            return ErrorCode.SUCCESS.toJsonString();
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
     * 获取投资人评论资费总金额
     * @param investorId
     * @return
     */
    @GetMapping(value = "/investor/getCommentAmount")
    public InvestorCommentAmountDto getCommentAmount(@RequestParam String investorId) {
        InvestorCommentAmountDto investorCommentAmountDto = new InvestorCommentAmountDto();
        List<ProjectComment> projectComments = mongoTemplate.find(query(Criteria.where("investorId").is(investorId).and("favor").ne(4)), ProjectComment.class);
        if (!CollectionUtils.isEmpty(projectComments)) {
            Integer unAccomplishedTimes = 0;
            Integer accomplishedTimes = 0;
            BigDecimal unaccomplishedAmount = new BigDecimal("0.00");
            BigDecimal accomplishedAmount = new BigDecimal("0.00");
            BigDecimal commentAmount = null;
            for (ProjectComment projectComment : projectComments) {
                // 根据是否完成：true-完成，false-未完成，判断评论金额是否获取
                commentAmount = projectComment.getCommentAmount() == null ? new BigDecimal("0.00") : projectComment.getCommentAmount();
                if (projectComment.getIsDone()) {
                    accomplishedAmount = accomplishedAmount.add(commentAmount);// 已获取金额
                    accomplishedTimes++;
                } else {
                    unAccomplishedTimes++;
                    unaccomplishedAmount = unaccomplishedAmount.add(commentAmount);// 未获取金额
                }
            }
            investorCommentAmountDto.setInvestorId(investorId);
            investorCommentAmountDto.setUnaccomplishedAmount(unaccomplishedAmount);
            investorCommentAmountDto.setAccomplishedAmount(accomplishedAmount);
            investorCommentAmountDto.setUnAccomplishedTimes(unAccomplishedTimes);
            investorCommentAmountDto.setAccomplishedTimes(accomplishedTimes);
        }
        return investorCommentAmountDto;
    }

    /**
     * 查询评论和项目详情
     * @param id
     * @return
     */
    @GetMapping(value = "/investor/getCommentProject")
    public String getCommentProject(@RequestParam String id) {
        try{
            CommentProjectDto commentProjectDto = new CommentProjectDto();
            // 获取评论信息
            List<ProjectComment> projectComments = mongoTemplate.find(query(Criteria.where("id").is(id)), ProjectComment.class);
            if (CollectionUtils.isEmpty(projectComments)) {
                return ErrorCode.NULLOBJECT.toJsonString();
            }
            ProjectComment projectComment = projectComments.get(0);
            Project project = null;
            List<Project> projects = mongoTemplate.find(query(Criteria.where("projectNo").is(projectComment.getProjectNo())), Project.class);
            if (!CollectionUtils.isEmpty(projects)) {
                project = projects.get(0);
            }
            if (null != project) {
                BeanUtils.copyProperties(project, commentProjectDto);
            }
            commentProjectDto.setId(projectComment.getId());
            commentProjectDto.setContent(projectComment.getContent());
            commentProjectDto.setFavor(projectComment.getFavor());
            OutputFormate outputFormate = new OutputFormate(commentProjectDto, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
            return JSONObject.toJSONString(outputFormate);
        }  catch (Exception e){
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 获取投资人首页信息
     * @param phoneNm
     * @return
     */
    @GetMapping(value = "/investor/getInvestorInfo")
    public String getInvestorInfo(@RequestParam(value = "phoneNm", required = true) String phoneNm) {
        Investor findInvestor = mongoTemplate.findOne(query(where("phoneNm").is(phoneNm)),Investor.class);
        if(!StringUtils.isEmpty(findInvestor)){
            // 获取投资人及机构图片信息
            findInvestor.setPhoto(commonUtils.getPhoto(findInvestor.getInvesPhotoRoute()));
            findInvestor.setOrgPhoto(commonUtils.getPhoto(findInvestor.getInvesOrgPhotoRoute()));

            // 获取投资人评论的金额总计信息
            InvestorCommentAmountDto investorCommentAmountDto = getCommentAmount(findInvestor.getInvestorId());
            findInvestor.setAccomplishedAmount(investorCommentAmountDto.getAccomplishedAmount());
            findInvestor.setUnaccomplishedAmount(investorCommentAmountDto.getUnaccomplishedAmount());
            findInvestor.setAccomplishedTimes(investorCommentAmountDto.getAccomplishedTimes());
            findInvestor.setUnAccomplishedTimes(investorCommentAmountDto.getUnAccomplishedTimes());

            OutputFormate outputFormate = new OutputFormate(findInvestor);
            return JSONObject.toJSONString(outputFormate);
        }else{
            return ErrorCode.NULLOBJECT.toJsonString();
        }
    }

    //    ---------------------------------项目一期PC版部分接口调整---------------------------------------- ------------

    /**
     * 分页获取评论信息（客户）
     * @param getCommentsDto
     * @return
     */
    @GetMapping(value = "/entuser/commentsByUserId")
    public String commentsByUserId(GetCommentsDto getCommentsDto){
        try{
            int pageNum = getCommentsDto.getPageNum();
            int pageSize = getCommentsDto.getPageSize();
            if (pageNum < 0 || pageSize <= 0 || StringUtils.isEmpty(getCommentsDto.getOpenId())) {
                return ErrorCode.PAGEBELLOWZERO.toJsonString();
            } else {
                int startNum = pageNum * pageSize;
                Criteria criteria = Criteria.where("userId").is(getCommentsDto.getUserId());
                if (!StringUtils.isEmpty(getCommentsDto.getProjectNo())) {
                    criteria = criteria.and("projectNo").is(getCommentsDto.getProjectNo());
                }
                Query query = new Query(criteria).with(Sort.by(Sort.Order.desc("isDone")))
                        .with(Sort.by(Sort.Order.asc("favor")))
                        .with(Sort.by(Sort.Order.asc("updateTm")));
                List<ProjectComment> projectComments = mongoTemplate.find(query.skip(startNum).limit(pageSize), ProjectComment.class);
                if (!CollectionUtils.isEmpty(projectComments)) {
                    for (ProjectComment projectComment : projectComments) {
                        projectComment.setPhoto(commonUtils.getPhoto(projectComment.getInvesPhotoRoute()));
                    }
                }
                OutputFormate outputFormate = new OutputFormate(projectComments, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
                return JSONObject.toJSONString(outputFormate);
            }
        }catch (Exception e){
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 查询评论和项目详情
     * @param projectNo
     * @return
     */
    @GetMapping(value = "/investor/projectCommentDetail")
    public String projectCommentDetail(@RequestParam String projectNo, String investorId) {
        try{
            ProjectCommentDetailDto projectCommentDetailDto = new ProjectCommentDetailDto();
            Query query = query(where("projectNo").is(projectNo));
            // 获取评论信息
            if (!StringUtils.isEmpty(investorId)) {
                query.addCriteria(where("investorId").is(investorId));
            }
            List<ProjectComment> projectComments = mongoTemplate.find(query.with(Sort.by(Sort.Order.asc("investorId"))).with(Sort.by(Sort.Order.asc("createTime"))), ProjectComment.class);
            if (CollectionUtils.isEmpty(projectComments)) {
                return ErrorCode.NULLOBJECT.toJsonString();
            }
            Project project = null;
            List<Project> projects = mongoTemplate.find(query(Criteria.where("projectNo").is(projectNo)), Project.class);
            if (!CollectionUtils.isEmpty(projects)) {
                project = projects.get(0);
                if (!StringUtils.isEmpty(project.getRoadshowRoute())) {
                    project.setRoadshow(commonUtils.getPhoto(project.getRoadshowRoute()));
                }
            }
            projectCommentDetailDto.setProject(project);
            projectCommentDetailDto.setProjectCommentList(projectComments);
            OutputFormate outputFormate = new OutputFormate(projectCommentDetailDto, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
            return JSONObject.toJSONString(outputFormate);
        }  catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    @PostMapping("/projectComment/markFavor")
    public String markFavor(@RequestBody ProjectComment projectComment){
        Update update = new Update();
        update.set("favor", projectComment.getFavor());
        update.set("updateTm", new Date());
        mongoTemplate.updateFirst(query(where("id").is(projectComment.getId())), update, ProjectComment.class);
        return ErrorCode.SUCCESS.toJsonString();
    }

    /**
     * 投资人提交评价
     * @param projectComment
     * @return
     */
    @PostMapping(value = "/investor/commitComment")
    public String commitComment(MultipartFile file, ProjectComment projectComment){
        try{
            // 上传项目
            String filePath = commentFilePath;
            if (null != file) {
                commonUtils.uploadData(file, filePath);
                projectComment.setCommentFilePath(filePath + "/" + file.getOriginalFilename());
            }

            // 提交评论内容
            Update update = new Update();
            update.set("isDone", true);
            if (null != projectComment.getFavor()) {
                update.set("favor", projectComment.getFavor());
            }
            update.set("content", projectComment.getContent());
            if (!StringUtils.isEmpty(projectComment.getCommentFilePath())) {
                update.set("commentFilePath", projectComment.getCommentFilePath());
            }
            update.set("updateTm", new Date());
            mongoTemplate.updateFirst(query(where("id").is(projectComment.getId())), update, ProjectComment.class);

            // 更新投资人剩余积分
            Investor investor = mongoTemplate.findOne(query(where("investorId").is(projectComment.getInvestorId())), Investor.class);
            BigDecimal surplusAmount = investor.getSurplusAmount() == null ? new BigDecimal("0.00") : investor.getSurplusAmount();
            surplusAmount = surplusAmount.add(projectComment.getCommentAmount());
            Update updateInvestor = new Update();
            updateInvestor.set("surplusAmount", surplusAmount);
            mongoTemplate.updateFirst(query(where("investorId").is(investor.getInvestor())), update, Investor.class);

            // 生成消息推送数据
            String msg = "";
            if (1 == projectComment.getCommentType()) {
                msg = "您有一条投资人[" + projectComment.getInvestor() + "]反馈待接收";
            } else if (2 == projectComment.getCommentType()) {
                msg = "您有一条追问投资人[" + projectComment.getInvestor() + "]已回复";
            }
            Message message = new Message();
            message.setId(commonUtils.getNumCode());
            message.setMsg(msg);
            message.setSender("系统消息");
            message.setCreateTime(new Date());
            message.setUserId(projectComment.getEntUserId());
            message.setStatus(0);
            messageService.add(message);
            return ErrorCode.SUCCESS.toJsonString();
        }catch (Exception e){
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }

    /**
     * 通过investorId获取投资人信息
     * @param investorId
     * @return
     */
    @GetMapping("/investor/investorByInvestorId")
    public String investorByInvestorId(@RequestParam String investorId) {
        Investor investor = mongoTemplate.findOne(query(where("investorId").is(investorId)), Investor.class);
        OutputFormate outputFormate = new OutputFormate(investor, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }
}
