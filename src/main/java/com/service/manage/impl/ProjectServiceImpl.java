package com.service.manage.impl;

import com.dto.indto.PageDto;
import com.dto.outdto.PageListDto;
import com.dto.outdto.ProjectDto;
import com.enums.CommentType;
import com.enums.RoleCode;
import com.pojo.*;
import com.service.manage.ProjectCommentService;
import com.service.manage.ProjectService;
import com.utils.CommonUtils;
import com.utils.DateUtil;
import com.utils.ErrorCode;
import org.omg.CORBA.DynAnyPackage.Invalid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ProjectCommentService projectCommentService;
    @Autowired
    private CommonUtils commonUtils;

    @Override
    public List<Project> pageList(Integer pageNum, Integer pageSize) {
        int startNum = pageNum * pageSize;
        List<Project> projects = mongoTemplate.find(new Query(where("isDone").is(true).and("showFlag").is(1)).with(Sort.by(Sort.Order.desc("topFlushTime"))).skip(startNum).limit(pageSize), Project.class);
//        if (!CollectionUtils.isEmpty(projects)) {
//            for (Project project : projects) {
//                List<Integer> commentTypes = mongoTemplate.findDistinct(query(where("projectNo").is(project.getProjectNo())), "commentType", "projectComment", Integer.class);
//                String commentTypeDesc = "";
//                if (!CollectionUtils.isEmpty(commentTypes)) {
//                    for (Integer commentType : commentTypes)
//                    commentTypeDesc = commentTypeDesc + "," + CommentType.getMessage(commentType);
//                }
//                ProjectBpApply projectBpApply = mongoTemplate.findOne(query(where("projectNo").is(project.getProjectNo())), ProjectBpApply.class);
//                if (null != projectBpApply) {
//                    commentTypeDesc = commentTypeDesc + "," + "定制商业计划书";
//                }
//
//                if (!StringUtils.isEmpty(commentTypeDesc)) {
//                    commentTypeDesc = commentTypeDesc.substring(1);
//                }
////                project.setCommentTypeDesc(commentTypeDesc);
//            }
//        }
        return projects;
    }

    @Override
    public ProjectDto detail(String projectNo, String userId) {
        ProjectDto projectDto = new ProjectDto();
        Project project = mongoTemplate.findOne(query(where("projectNo").is(projectNo)), Project.class);
        if (null != project) {
            BeanUtils.copyProperties(project, projectDto);
            // 获取用户信息
            User user = mongoTemplate.findOne(query(where("userId").is(project.getEntUserId())), User.class);
            if (null != user) {
                projectDto.setUserId(user.getUserId());
                projectDto.setUserName(user.getUserName());
                projectDto.setPositionName(user.getPositionName());
                projectDto.setCompanyName(user.getCompanyName());
                projectDto.setPhotoRoute(user.getPhotoRoute());
                projectDto.setIsVerify(user.getIsVerify());
                projectDto.setRoleCode(user.getRoleCode());
            }

            // 通过userId查询用户信息,用户为投资人/FA时，记录该项目的浏览历史
            User investorUser = mongoTemplate.findOne(query(where("userId").is(userId)), User.class);
            if (null != investorUser && (RoleCode.INVESTOR.getCode().equals(investorUser.getRoleCode()) || RoleCode.FINANCIALADVISOR.getCode().equals(investorUser.getRoleCode()))) {
                // 查询是否已被浏览过
                BrowseHistory browseHistoryResp = mongoTemplate.findOne(query(where("userId").is(userId).and("projectNo")
                        .is(project.getProjectNo())), BrowseHistory.class);
                if (null != browseHistoryResp) {
                    Update update = new Update();
                    update.set("updateTime", new Date());
                    update.set("browseTimes", browseHistoryResp.getBrowseTimes() + 1);
                    mongoTemplate.updateFirst(query(where("userId").is(userId).and("projectNo")
                            .is(project.getProjectNo())), update, BrowseHistory.class);
                } else {
                    BrowseHistory browseHistory = new BrowseHistory();
                    BeanUtils.copyProperties(project, browseHistory);
                    browseHistory.setId(commonUtils.getNumCode());
                    browseHistory.setCreateTime(new Date());
                    browseHistory.setUpdateTime(new Date());
                    browseHistory.setBrowseTimes(1);
                    browseHistory.setUserId(userId);
                    browseHistory.setRoleCode(investorUser.getRoleCode());
                    mongoTemplate.insert(browseHistory, "browseHistory");
                }

                // 查询该项目是否已被投资人收藏
                Collection collection = mongoTemplate.findOne(query(where("projectNo").is(project.getProjectNo()).and("userId").is(userId)), Collection.class);
                if (null != collection) {
                    projectDto.setIsCollection(true);
                } else {
                    projectDto.setIsCollection(false);
                }

                // 查询该创业者是否已被投资人关注
                Attention attention = mongoTemplate.findOne(query(where("userId").is(userId)
                        .and("attentionUserId").is(project.getEntUserId())), Attention.class);
                if (null != attention) {
                    projectDto.setIsAttention(true);
                } else {
                    projectDto.setIsAttention(false);
                }
            }
        }

        return projectDto;
    }

    @Override
    public Integer count() {
        return (int) mongoTemplate.count(new Query(where("isDone").is(true)),"project");
    }

    @Override
    public List<Project> listByEnt(String openId) {
        return mongoTemplate.find(query(where("openId").is(openId).and("isDone").is(true)), Project.class);
    }

    @Override
    public void delete(String projectNo) {
        mongoTemplate.remove(query(where("projectNo").is(projectNo)), Project.class);
    }

    @Override
    public void status(String projectNo, Integer status) {
        Update update = new Update();
        update.set("status", status);
        mongoTemplate.updateFirst(query(where("projectNo").is(projectNo)), update, Project.class);
    }

    @Override
    public void edit(Project project) {
        // 获取待更新的项目信息
        Project orgProject = mongoTemplate.findOne(query(where("projectNo").is(project.getProjectNo())), Project.class);
        if (StringUtils.isEmpty(project.getBpRoute())) {
            project.setBpRoute(orgProject.getBpRoute());
        }
        if (StringUtils.isEmpty(project.getLogoRoute())) {
            project.setLogoRoute(orgProject.getLogoRoute());
        }
        project.setCreateTime(orgProject.getCreateTime());
        project.setUpdateTime(new Date());

        // 先删除原项目,再重新插入一笔同projectNo的项目
        mongoTemplate.remove(query(where("projectNo").is(project.getProjectNo())), Project.class);
        mongoTemplate.save(project);
    }

    @Override
    public void topProject(String userId) throws Exception {
        // 通过userId查询项目信息
        List<Project> projectList = mongoTemplate.find(query(where("entUserId").is(userId)), Project.class);
        if (CollectionUtils.isEmpty(projectList)) {
            throw new Exception(ErrorCode.PROJECTEMPTY.getMessage());
        }
        Project project = projectList.get(0);
        if (StringUtils.isEmpty(project.getBpRoute())) {
            throw new Exception(ErrorCode.BPROUTEEMPTY.getMessage());
        }
        Date topFlushTime = project.getTopFlushTime();
        if (null != topFlushTime && DateUtil.getDiffDate(topFlushTime, new Date(), 4) == 0) {
            throw new Exception(ErrorCode.TOPPROJECT.getMessage());
        }
        String projectNo = project.getProjectNo();
        Update update = new Update();
        update.set("topFlushTime", new Date());
        mongoTemplate.upsert(query(where("projectNo").is(projectNo)), update, Project.class);
    }

    @Override
    public PageListDto pageListByInvestor(PageDto pageDto) {
        PageListDto pageListDto = new PageListDto();
        // 1,查询投资人信息
        Investor investor =  mongoTemplate.findOne(query(where("investorId").is(pageDto.getUserId())), Investor.class);
        if (null == investor) {
            pageListDto = getProjectList(pageDto);
        } else {
            pageListDto = getMatchedProjectList(pageDto, investor);
        }
        return pageListDto;
    }

    /**
     * 获取非智能匹配项目列表
     * @param pageDto
     * @return
     */
    public PageListDto getProjectList(PageDto pageDto) {
        int pageNum = pageDto.getPageNum();
        int pageSize = pageDto.getPageSize();

        Query query = new Query();
        query.addCriteria(where("isDone").is(true).and("showFlag").is(1));
        // 行业
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

        // 轮次
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

        int count = (int) mongoTemplate.count(query, Project.class);

        int totalPage = count/pageSize;
        PageListDto pageListDto = new PageListDto<Investor>();
        pageListDto.setTotal(count);
        if(pageNum <= totalPage){
            // 按isPlatform排序，将平台投资人置前
            int startNum = pageNum * pageSize;
            List<Project> projects = mongoTemplate.find(query.skip(startNum).limit(pageSize), Project.class);
            pageListDto.setList(projects);
        }
        return pageListDto;
    }

    /**
     * 获取智能匹配项目列表
     * @param pageDto
     * @return
     */
    public PageListDto getMatchedProjectList(PageDto pageDto, Investor investor) {
        int pageNum = pageDto.getPageNum() + 1;
        int pageSize = pageDto.getPageSize();
        List<Project> projectList = new ArrayList<>();// 全部投资人列表
        List<Project> accurateList = null;// 精确匹配上的投资人列表
        List<Project> matchedList = null;// 智能匹配上的投资人列表
        List<Project> unMatchList = null;// 未匹配上的投资人列表
        List<String> projectNoList = new ArrayList<>();

        // 精确匹配查询条件
        Query accurateQuery = new Query();
        accurateQuery.addCriteria(where("isDone").is(true).and("showFlag").is(1));

        // 智能匹配查询条件
        Query matchedQuery = new Query();
        Pattern pattern = null;
        matchedQuery.addCriteria(where("isDone").is(true));
        List<Criteria> proCriteriaList = new ArrayList<>();
        if (!StringUtils.isEmpty(investor.getFocusFiled())) {
            List<String> focusFiledList = Arrays.asList(investor.getFocusFiled().split(","));
            proCriteriaList.add(Criteria.where("focusFiled").in(focusFiledList));
            accurateQuery.addCriteria(where("focusFiled").in(focusFiledList));
        }
        if (!StringUtils.isEmpty(investor.getFinRound())) {
            List<String> finRoundList = Arrays.asList(investor.getFinRound().split(","));
            proCriteriaList.add(Criteria.where("finRound").in(finRoundList));
            accurateQuery.addCriteria(where("finRound").in(finRoundList));
        }

        // 精确匹配查询
        accurateList = mongoTemplate.find(accurateQuery, Project.class);
        if (!CollectionUtils.isEmpty(accurateList)) {
            projectList.addAll(accurateList);
            for (Project project : accurateList) {
                projectNoList.add(project.getProjectNo());
            }
        }

        Criteria proCriteria = null;
        if (!CollectionUtils.isEmpty(proCriteriaList)) {
            proCriteria = new Criteria().orOperator(proCriteriaList.toArray(new Criteria[0]));
        }
        // 行业
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

        // 轮次
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

        // 智能匹配查询（在未精准匹配列表）
        matchedList = mongoTemplate.find(matchedQuery.addCriteria(where("projectNo").nin(projectNoList)), Project.class);

        if (!CollectionUtils.isEmpty(matchedList)) {
            projectList.addAll(matchedList);
            for (Project project : matchedList) {
                projectNoList.add(project.getProjectNo());
            }
        }

        // 其他未匹配的查询条件
        Query otherQuery = new Query();
        otherQuery.addCriteria(where("isDone").is(true).and("projectNo").nin(projectNoList));

        if (null != proIndusCriteria && null != finRoundCriteria) {
            otherQuery.addCriteria(new Criteria().andOperator(proIndusCriteria, finRoundCriteria));
        } else if (null != proIndusCriteria && null == finRoundCriteria) {
            otherQuery.addCriteria(new Criteria().andOperator(proIndusCriteria));
        } else if (null == proIndusCriteria && null != finRoundCriteria) {
            otherQuery.addCriteria(new Criteria().andOperator(finRoundCriteria));
        }
        unMatchList = mongoTemplate.find(otherQuery, Project.class);
        if (!CollectionUtils.isEmpty(unMatchList)) {
            projectList.addAll(unMatchList);
        }

        int count = 0;
        PageListDto pageListDto = new PageListDto<Investor>();
        if (CollectionUtils.isEmpty(projectList)) {
            pageListDto.setTotal(count);
            return pageListDto;
        }

        count = projectList.size();
        Integer pageCount = 0; // 页数
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }

        int fromIndex = 0; // 开始索引
        int toIndex = 0; // 结束索引

        if (pageNum != pageCount) {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = fromIndex + pageSize;
        } else {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = count;
        }
        projectList = projectList.subList(fromIndex, toIndex);
        pageListDto.setList(projectList);
        pageListDto.setTotal(count);

        return pageListDto;
    }

}
