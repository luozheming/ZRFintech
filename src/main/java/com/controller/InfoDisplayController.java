package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.dto.indto.OrderDto;
import com.dto.indto.PageDto;
import com.dto.indto.ProjectBpApplyDto;
import com.dto.indto.ProjectFormUploadDto;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.dto.outdto.ProjectDto;
import com.pojo.*;
import com.service.OrderService;
import com.service.ProjectDeliverService;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;


@RestController
public class InfoDisplayController {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommonUtils commonUtils;

    @Value("${savedfilepath}")
    private String savedfilepath;

    @Value("${roadshowFilePath}")
    private String roadshowFilePath;

    @Value("${s3BucketName}")
    private String s3BucketName;

    @Autowired
    private ProjectDeliverService projectDeliverService;

    private static final Logger logger = LoggerFactory.getLogger(InfoDisplayController.class);

    /**
     * ????????????????????????
     * @param pageDto
     * @return
     */
    @PostMapping(value = "/investor/getProjectInfo")
    public String getProjectInfo(@RequestBody PageDto pageDto) {
        int pageNum = pageDto.getPageNum();
        int pageSize = pageDto.getPageSize();
        //????????????
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
     * ?????????????????????????????????
     * @param pageDto
     * @return
     */
    @PostMapping(value = "/entuser/getInvestorInfo")
    public String getInvestorInfo(@RequestBody PageDto pageDto) {
        logger.info("??????????????????????????????" + JSONObject.toJSONString(pageDto));
        int pageNum = pageDto.getPageNum();
        int pageSize = pageDto.getPageSize();
        //????????????
        if (pageNum < 0 || pageSize <= 0) {
            return ErrorCode.PAGEBELLOWZERO.toJsonString();
        } else {
            // ????????????????????????
            Project project = mongoTemplate.findOne(query(where("entUserId").is(pageDto.getUserId()).and("isDone").is(true)), Project.class);
            logger.info("????????????????????????????????????????????????" + JSONObject.toJSONString(project));
            PageListDto pageListDto = null;
            if (null != project) {
                pageListDto = getMatchedInvestorList(pageDto, project);
            } else {
                pageListDto = getInvestorList(pageDto);
            }
            if (null != pageListDto && !CollectionUtils.isEmpty(pageListDto.getList())) {
                List<Investor> investorList = pageListDto.getList();
                for (Investor investor : investorList) {
                    User user = mongoTemplate.findOne(query(where("userId").is(investor.getInvestorId())), User.class);
                    if (null != user) {
                        investor.setIsVerify(user.getIsVerify());
                    }
                }
            }
            OutputFormate outputFormate = new OutputFormate(pageListDto);
            return JSONObject.toJSONString(outputFormate);
        }
    }

    /**
     * ????????????????????????????????????
     * @param pageDto
     * @return
     */
    public PageListDto getInvestorList(PageDto pageDto) {
        int pageNum = pageDto.getPageNum();
        int pageSize = pageDto.getPageSize();

        Query query = new Query();
        query.addCriteria(where("status").is(0).and("showFlag").is(1));
        // ??????
        Criteria proIndusCriteria = null;
        if (!StringUtils.isEmpty(pageDto.getProIndus())) {
            List<String> proIndusList = Arrays.asList(pageDto.getProIndus().split(","));
            List<Criteria> orCriterias = new ArrayList<>();
            Pattern pattern = null;
            for (String proIndus : proIndusList) {
                pattern = Pattern.compile("^.*"+proIndus+".*$", Pattern.CASE_INSENSITIVE);
                orCriterias.add(Criteria.where("focusFiled").regex(pattern));
            }
            proIndusCriteria = new Criteria().orOperator(orCriterias.toArray(new Criteria[0]));
        }

        // ??????
        Criteria finRoundCriteria = null;
        if (!StringUtils.isEmpty(pageDto.getFinRound())) {
            List<String> finRoundList = Arrays.asList(pageDto.getFinRound().split(","));
            List<Criteria> orCriterias = new ArrayList<>();
            Pattern pattern = null;
            for (String finRound : finRoundList) {
                pattern = Pattern.compile("^.*"+finRound+".*$", Pattern.CASE_INSENSITIVE);
                orCriterias.add(Criteria.where("finRound").regex(pattern));
            }
            finRoundCriteria = new Criteria().orOperator(orCriterias.toArray(new Criteria[0]));
        }

        if (null != proIndusCriteria && null != finRoundCriteria) {
            query.addCriteria(new Criteria().andOperator(proIndusCriteria, finRoundCriteria));
        } else if (null != proIndusCriteria && null == finRoundCriteria) {
            query.addCriteria(new Criteria().andOperator(proIndusCriteria));
        } else if (null == proIndusCriteria && null != finRoundCriteria) {
            query.addCriteria(new Criteria().andOperator(finRoundCriteria));
        }

        int count = (int) mongoTemplate.count(query, Investor.class);

        int totalPage = count/pageSize;
        PageListDto pageListDto = new PageListDto<Investor>();
        pageListDto.setTotal(count);
        if(pageNum <= totalPage){
            // ???isPlatform?????????????????????????????????
            int startNum = pageNum * pageSize;
            List<Investor> investors = mongoTemplate.find(query.skip(startNum).limit(pageSize), Investor.class);
            pageListDto.setList(investors);
        }
        return pageListDto;
    }

    /**
     * ?????????????????????????????????
     * @param pageDto
     * @return
     */
    public PageListDto getMatchedInvestorList(PageDto pageDto, Project project) {
        int pageNum = pageDto.getPageNum() + 1;
        int pageSize = pageDto.getPageSize();
        List<Investor> investorList = new ArrayList<>();// ?????????????????????
        List<Investor> accurateList = null;// ?????????????????????????????????
        List<Investor> matchedList = null;// ?????????????????????????????????
        List<Investor> unMatchList = null;// ??????????????????????????????
        List<String> investorIdList = new ArrayList<>();

        // ????????????????????????
        Query accurateQuery = new Query();
        accurateQuery.addCriteria(where("status").is(0).and("showFlag").is(1));

        // ????????????????????????
        Query matchedQuery = new Query();
        Pattern pattern = null;
        matchedQuery.addCriteria(where("status").is(0).and("showFlag").is(1));
        List<Criteria> proCriteriaList = new ArrayList<>();
        if (!StringUtils.isEmpty(project.getProIndus())) {
            pattern = Pattern.compile("^.*"+project.getProIndus()+".*$", Pattern.CASE_INSENSITIVE);
            proCriteriaList.add(Criteria.where("focusFiled").regex(pattern));
            accurateQuery.addCriteria(where("focusFiled").regex(pattern));
        }
        if (!StringUtils.isEmpty(project.getFinRound())) {
            pattern = Pattern.compile("^.*"+project.getFinRound()+".*$", Pattern.CASE_INSENSITIVE);
            proCriteriaList.add(Criteria.where("finRound").regex(pattern));
            accurateQuery.addCriteria(where("finRound").regex(pattern));
        }

        // ??????????????????
        accurateList = mongoTemplate.find(accurateQuery, Investor.class);
        if (!CollectionUtils.isEmpty(accurateList)) {
            investorList.addAll(accurateList);
            for (Investor investor : accurateList) {
                investorIdList.add(investor.getInvestorId());
            }
        }

        Criteria proCriteria = null;
        if (!CollectionUtils.isEmpty(proCriteriaList)) {
            proCriteria = new Criteria().orOperator(proCriteriaList.toArray(new Criteria[0]));
        }
        // ??????
        List<Criteria> proIndusCriterias = new ArrayList<>();
        if (!StringUtils.isEmpty(pageDto.getProIndus())) {
            List<String> proIndusList = Arrays.asList(pageDto.getProIndus().split(","));
            for (String proIndus : proIndusList) {
                pattern = Pattern.compile("^.*"+proIndus+".*$", Pattern.CASE_INSENSITIVE);
                proIndusCriterias.add(Criteria.where("focusFiled").regex(pattern));
            }
        }
        Criteria proIndusCriteria = null;
        if (!CollectionUtils.isEmpty(proIndusCriterias)) {
            proIndusCriteria = new Criteria().orOperator(proIndusCriterias.toArray(new Criteria[0]));
        }

        // ??????
        List<Criteria> finRoundCriterias = new ArrayList<>();
        if (!StringUtils.isEmpty(pageDto.getFinRound())) {
            List<String> finRoundList = Arrays.asList(pageDto.getFinRound().split(","));
            for (String finRound : finRoundList) {
                pattern = Pattern.compile("^.*"+finRound+".*$", Pattern.CASE_INSENSITIVE);
                finRoundCriterias.add(Criteria.where("finRound").regex(pattern));
            }
        }
        Criteria finRoundCriteria = null;
        if (!CollectionUtils.isEmpty(finRoundCriterias)) {
            finRoundCriteria = new Criteria().orOperator(finRoundCriterias.toArray(new Criteria[0]));
        }
        if (null != proCriteria && null != proIndusCriteria && null != finRoundCriteria) {
            matchedQuery.addCriteria(new Criteria().andOperator(proCriteria, proIndusCriteria, finRoundCriteria));
        } else if (null != proCriteria && null == proIndusCriteria && null != finRoundCriteria) {
            matchedQuery.addCriteria(new Criteria().andOperator(proCriteria, finRoundCriteria));
        } else if (null != proCriteria && null != proIndusCriteria && null == finRoundCriteria) {
            matchedQuery.addCriteria(new Criteria().andOperator(proCriteria, proIndusCriteria));
        } else if (null != proCriteria && null == proIndusCriteria && null == finRoundCriteria) {
            matchedQuery.addCriteria(new Criteria().andOperator(proCriteria));
        } else if (null == proCriteria && null != proIndusCriteria && null != finRoundCriteria) {
            matchedQuery.addCriteria(new Criteria().andOperator(proIndusCriteria, finRoundCriteria));
        } else if (null == proCriteria && null != proIndusCriteria && null == finRoundCriteria) {
            matchedQuery.addCriteria(new Criteria().andOperator(proIndusCriteria));
        } else if (null == proCriteria && null == proIndusCriteria && null != finRoundCriteria) {
            matchedQuery.addCriteria(new Criteria().andOperator(finRoundCriteria));
        }

        // ????????????????????????????????????????????????
        matchedList = mongoTemplate.find(matchedQuery.addCriteria(where("investorId").nin(investorIdList)), Investor.class);

        if (!CollectionUtils.isEmpty(matchedList)) {
            investorList.addAll(matchedList);
            for (Investor investor : matchedList) {
                investorIdList.add(investor.getInvestorId());
            }
        }

        // ??????????????????????????????
        Query otherQuery = new Query();
        otherQuery.addCriteria(where("status").is(0).and("showFlag").is(1).and("investorId").nin(investorIdList));
        if (null != proIndusCriteria && null != finRoundCriteria) {
            otherQuery.addCriteria(new Criteria().andOperator(proIndusCriteria, finRoundCriteria));
        } else if (null != proIndusCriteria && null == finRoundCriteria) {
            otherQuery.addCriteria(new Criteria().andOperator(proIndusCriteria));
        } else if (null == proIndusCriteria && null != finRoundCriteria) {
            otherQuery.addCriteria(new Criteria().andOperator(finRoundCriteria));
        }
        unMatchList = mongoTemplate.find(otherQuery, Investor.class);
        if (!CollectionUtils.isEmpty(unMatchList)) {
            investorList.addAll(unMatchList);
        }

        int count = 0;
        PageListDto pageListDto = new PageListDto<Investor>();
        if (CollectionUtils.isEmpty(investorList)) {
            pageListDto.setTotal(count);
            return pageListDto;
        }

        count = investorList.size();
        Integer pageCount = 0; // ??????
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }

        int fromIndex = 0; // ????????????
        int toIndex = 0; // ????????????

        if (pageNum != pageCount) {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = fromIndex + pageSize;
        } else {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = count;
        }
        investorList = investorList.subList(fromIndex, toIndex);
        pageListDto.setList(investorList);
        pageListDto.setTotal(count);

        return pageListDto;
    }

    /**
     *  ????????????????????????
     */
    @PostMapping(value = "/project/getdraftbyid")
    public String projectFormUpload(@RequestBody ProjectFormUploadDto projectFormUploadDto){
        //??????????????????
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("entUserId").is(projectFormUploadDto.getEntUserId()), Criteria.where("openId").is(projectFormUploadDto.getOpenId()));
        criteria.and("isDone").is(false);
        Query query = new Query(criteria);
        Project draftProject = mongoTemplate.findOne(query, Project.class);
        OutputFormate outputFormate = new OutputFormate(draftProject);
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     * ????????????????????????
     */
    @PostMapping(value = "/project/uploadproject")
    public String upLoadProject(MultipartFile file, MultipartFile logoFile, Project project) {
        logger.info("?????????????????????" + JSONObject.toJSONString(project));
        String projectNo = (project.getIsDone() != null && project.getIsDone()) ? commonUtils.getNumCode() : null;// ????????????ID
        //??????????????????????????????
        if (null != file) {
            // ???????????????
            String fileName = file.getOriginalFilename();
            // ????????????????????????
            StringBuilder filePathBuffer = new StringBuilder();
            String filePath = filePathBuffer.append(savedfilepath).append(project.getOpenId()).append("/").append(null == projectNo ? "temp" : projectNo).append("/").toString();
            try {
                // AWS S3????????????
                commonUtils.uploadFile(s3BucketName,filePath + file.getOriginalFilename(), file.getBytes());
                //????????????????????????????????????
                project.setBpRoute(filePath + fileName);
            } catch (Exception e) {
                return ErrorCode.FILEUPLOADFAILED.toJsonString();
            }
        }

        //??????????????????????????????
        if (null != logoFile) {
            // ???????????????
            String fileName = logoFile.getOriginalFilename();
            // ????????????????????????
            StringBuilder filePathBuffer = new StringBuilder();
            String filePath = filePathBuffer.append(savedfilepath).append(project.getOpenId()).append("/").append(null == projectNo ? "temp" : projectNo).append("/").toString();
            try {
                // AWS S3????????????
                commonUtils.uploadFile(s3BucketName,filePath + logoFile.getOriginalFilename(), logoFile.getBytes());
                //????????????????????????????????????
                project.setLogoRoute(filePath + fileName);
            } catch (Exception e) {
                return ErrorCode.FILEUPLOADFAILED.toJsonString();
            }
        }

        //???????????????????????????????????????????????????????????????id
        if (project.getIsDone() != null && !project.getIsDone()) {
            //??????????????????????????????????????????????????????????????????????????????
            Criteria criteria = new Criteria();
            criteria.orOperator(Criteria.where("entUserId").is(project.getEntUserId()), Criteria.where("openId").is(project.getOpenId()));
            criteria.and("isDone").is(false);
            Query query = new Query(criteria);
            mongoTemplate.update(Project.class)
                    .matching(query)
                    .replaceWith(project)
                    .withOptions(FindAndReplaceOptions.options().upsert())
                    .findAndReplace();
            return ErrorCode.SUCCESS.toJsonString();
        } else {
            //?????????????????????????????????
            project.setProjectNo(projectNo);
            project.setCreateTime(new Date());
            mongoTemplate.save(project);

            // ??????????????????????????????????????????
            Criteria criteria = new Criteria();
            criteria.orOperator(Criteria.where("entUserId").is(project.getEntUserId()), Criteria.where("openId").is(project.getOpenId()));
            criteria.and("isDone").is(false);
            Query query = new Query(criteria);

            mongoTemplate.remove(query, Project.class);
            Project outputProject = Project.builder().projectNo(project.getProjectNo()).projectNm(project.getProjectNm()).build();
            OutputFormate outputFormate = new OutputFormate(outputProject);
            return JSONObject.toJSONString(outputFormate);
        }
    }

    /**
     * ??????bp??????
     * @param projectBpApplyDto
     * @return
     */
    @PostMapping("/project/bpApply")
    public String bpApply(@RequestBody ProjectBpApplyDto projectBpApplyDto) {
        ProjectBpApply projectBpApply = new ProjectBpApply();
        try {
            BeanUtils.copyProperties(projectBpApplyDto, projectBpApply);

            // 1,????????????
            String id = commonUtils.getNumCode();// BP????????????id
            projectBpApply.setId(id);
            projectBpApply.setCreateTime(new Date());
            projectBpApply.setDealStatus(0);
            mongoTemplate.save(projectBpApply);

            // 2,????????????
            OrderDto orderDto = new OrderDto();
            orderDto.setBizId(projectBpApply.getId());
            orderDto.setBizType(projectBpApply.getApplyType());
            orderDto.setOpenId(projectBpApply.getOpenId());
            orderDto.setUserId(projectBpApplyDto.getEntUserId());
            orderService.createOrder(orderDto);
        } catch (Exception e) {
            return JSONObject.toJSONString(ErrorCode.OTHEREEEOR);
        }
        OutputFormate outputFormate = new OutputFormate(projectBpApply);
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     * ?????????????????????
     */
    @GetMapping("/project/getMyProjects")
    public String getMyProjects(String openId, String entUserId){
        logger.info("???????????????????????????" + entUserId);
        ProjectDto projectDto = new ProjectDto();
        Criteria criteria = new Criteria();
//        criteria.orOperator(Criteria.where("entUserId").is(entUserId), Criteria.where("openId").is(openId));
        criteria.and("entUserId").is(entUserId);
        criteria.and("isDone").is(true);
        Query query = new Query(criteria);
        query.with(Sort.by(Sort.Order.asc("createTime")));
        List<Project> projectList = mongoTemplate.find(query, Project.class);
        logger.info("projectList????????????" + projectList.size());
        if (!CollectionUtils.isEmpty(projectList)) {
            Project project = projectList.get(0);
            BeanUtils.copyProperties(project, projectDto);

            // ??????????????????
            User user = mongoTemplate.findOne(query(where("userId").is(entUserId)), User.class);
            if (null != user) {
                projectDto.setUserName(user.getUserName());
                projectDto.setPositionName(user.getPositionName());
                projectDto.setCompanyName(user.getCompanyName());
                projectDto.setPhotoRoute(user.getPhotoRoute());
                projectDto.setIsVerify(user.getIsVerify());
            }
        }
        OutputFormate outputFormate = new OutputFormate(projectDto);
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     * ??????????????????
     */
    @PostMapping("/project/deleteMyProject")
    public String deleteMyProject(@RequestParam(value = "projectNo", required = true) String projectNo){
        //???????????????????????????
        mongoTemplate.findAndRemove(query(where("projectNo").is(projectNo)),Project.class);
        return ErrorCode.SUCCESS.toJsonString();
    }


    //    ---------------------------------????????????PC?????????????????????----------------------------------------------------

    /**
     * ????????????????????????
     * @param userId
     * @return
     */
    @GetMapping(value = "/project/draftByUserId")
    public String draftByPhoneNm(@RequestParam String userId){
        //??????????????????
        Project draftProject = mongoTemplate.findOne(query(where("entUserId").is(userId).and("isDone").is(false)),Project.class);
        OutputFormate outputFormate = new OutputFormate(draftProject);
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     * ?????????????????????
     */
    @GetMapping("/project/projectList")
    public String projectList(@RequestParam(value = "userId", required = true) String userId){
        List<Project> projectList = mongoTemplate.find(query(where("entUserId").is(userId).and("isDone").is(true)), Project.class);
        OutputFormate outputFormate = new OutputFormate(projectList);
        return JSONObject.toJSONString(outputFormate);
    }

    /**
     * ????????????????????????
     */
    @PostMapping(value = "/project/createProject")
    public String createProject(@RequestPart(value = "file", required = false) MultipartFile file, Project project) {
        String projectNo = (project.getIsDone() != null && project.getIsDone()) ? commonUtils.getNumCode() : null;// ????????????ID
        //??????????????????????????????
        if (null != file) {
            // ???????????????
            String fileName = file.getOriginalFilename();
            // ????????????????????????
            //String suffixName = fileName.substring(fileName.lastIndexOf("."));
            // ????????????????????????
            StringBuilder filePathBuffer = new StringBuilder();
            String filePath = filePathBuffer.append(savedfilepath).append(project.getEntUserId()).append("/").append(null == projectNo ? "temp" : projectNo).append("/").toString();
            File dest = new File(filePath + fileName);
            // ????????????????????????
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(dest);
                //????????????????????????????????????
                project.setBpRoute(filePath + fileName);
            } catch (IllegalStateException e) {
                return ErrorCode.FILEUPLOADFAILED.toJsonString();
            } catch (IOException e) {
                return ErrorCode.FILEUPLOADFAILED.toJsonString();
            }
        }

        //???????????????????????????????????????????????????????????????id
        if (project.getIsDone() != null && !project.getIsDone()) {
            //??????????????????????????????????????????????????????????????????????????????
            mongoTemplate.update(Project.class)
                    .matching(query(where("entUserId").is(project.getEntUserId()).and("isDone").is(false)))
                    .replaceWith(project)
                    .withOptions(FindAndReplaceOptions.options().upsert())
                    .findAndReplace();
            return ErrorCode.SUCCESS.toJsonString();
        } else {
            //?????????????????????????????????
            project.setProjectNo(projectNo);
            mongoTemplate.save(project);
            Project outputProject = Project.builder().projectNo(project.getProjectNo()).projectNm(project.getProjectNm()).build();
            OutputFormate outputFormate = new OutputFormate(outputProject);
            return JSONObject.toJSONString(outputFormate);
        }
    }

    /**
     * ???????????????????????????????????????
     * @param openId
     * @return
     */
    @PostMapping("/entuser/browseProjectPage")
    public String browseProjectPage(String openId, Integer projectType) {
        Update update = new Update();
//        if (1 == projectType) {
//            update.set("isBrowse", true);
//        } else if (2 == projectType) {
//            update.set("isRoadShowBrowse", true);
//        }
        update.set("isBrowse", true);
        mongoTemplate.updateFirst(query(where("openId").is(openId)), update, EntUser.class);
        return ErrorCode.SUCCESS.toJsonString();
    }


    /**
     * ??????????????????
     * @param file
     * @return
     */
    @PostMapping("/entuser/uploadRoadShowFile")
    public String uploadRoadShowFile(MultipartFile file, Project project) {
        if (StringUtils.isEmpty(project.getProjectNo())) {
            project.setProjectNo(commonUtils.getNumCode());
        }
        //??????????????????????????????
        if (null != file) {
            // ???????????????
            String fileName = file.getOriginalFilename();
            // ????????????????????????
            StringBuilder filePathBuffer = new StringBuilder();
            String filePath = filePathBuffer.append(roadshowFilePath).append(project.getEntUserId()).append("/").append(project.getProjectNo()).append("/").toString();
            File dest = new File(filePath + fileName);
            // ????????????????????????
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(dest);
                //????????????????????????????????????
                project.setRoadshowRoute(filePath + fileName);
                OutputFormate outputFormate = new OutputFormate(project, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
                return JSONObject.toJSONString(outputFormate);
            } catch (IllegalStateException e) {
                return ErrorCode.FILEUPLOADFAILED.toJsonString();
            } catch (IOException e) {
                return ErrorCode.FILEUPLOADFAILED.toJsonString();
            }
        } else {
            return ErrorCode.EMPITYFILE.toJsonString();
        }
    }
}