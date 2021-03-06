package com.service.manage.impl;

import com.dto.indto.EntUserPageListDto;
import com.dto.indto.InvestorEditDto;
import com.dto.indto.PageDto;
import com.dto.indto.SendEmailDto;
import com.dto.outdto.EntUserDto;
import com.dto.outdto.HomePageDto;
import com.dto.outdto.PageListDto;
import com.enums.RoleCode;
import com.pojo.*;
import com.service.EmailService;
import com.service.manage.UserService;
import com.utils.CommonUtils;
import com.utils.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Value("${internalPhoneNo}")
    private String internalPhoneNo;
    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    private EmailService emailService;
    @Value("${platform.adviser.email}")
    private String adviserEmail;

    @Override
    public User login(String phoneNm, String password) {
        User user = mongoTemplate.findOne(query(where("phoneNm").is(phoneNm).and("password").is(password)), User.class);
        return  user;
    }

    @Override
    public User getById(String userId) {
        User user = mongoTemplate.findOne(query(where("userId").is(userId)), User.class);
        return  user;
    }

    @Override
    public HomePageDto homePage() {
        // ???????????????????????????????????????????????????
        HomePageDto homePageDto = new HomePageDto();
        int entUserCount = (int) mongoTemplate.count(new Query(where("phoneNm").nin(internalPhoneNo.split(","))), "entuser");
        int isBrowseCount = (int) mongoTemplate.count(new Query(where("isBrowse").is(true).and("phoneNm").nin(internalPhoneNo.split(","))), "entuser");
        int isRoadShowBrowseCount = (int) mongoTemplate.count(new Query(where("isRoadShowBrowse").is(true).and("phoneNm").nin(internalPhoneNo.split(","))), "entuser");

        int bpCount = 0;
        int roadShowCount = 0;
        int isPayCount = 0;
        List<Project> projects = mongoTemplate.find(query(where("isDone").is(true)), Project.class);
        if (!CollectionUtils.isEmpty(projects)) {
            for (Project project : projects) {
                EntUser entUser = mongoTemplate.findOne(query(where("openId").is(project.getOpenId()).and("phoneNm").nin(internalPhoneNo.split(","))), EntUser.class);
                if (null != entUser) {
                    bpCount ++;

                    if (null != project.getIsPay() && project.getIsPay()) {
                        isPayCount ++;
                    }
                }
            }
        }

        // ???????????????????????????
        String date = DateUtil.dateToStr(new Date(), "yyyy-MM-dd");
        Date dateStart = DateUtil.strToDate(date + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date dateEnd = DateUtil.strToDate(date + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        int entUserAddCountPerDay= (int) mongoTemplate.count(query(where("createTime").gt(dateStart).lt(dateEnd).and("phoneNm").nin(internalPhoneNo.split(","))), EntUser.class);
        homePageDto.setEntUserCount(entUserCount);
        homePageDto.setIsBrowseCount(isBrowseCount);
        homePageDto.setIsRoadShowBrowseCount(isRoadShowBrowseCount);
        homePageDto.setBpCount(bpCount);
        homePageDto.setRoadShowCount(roadShowCount);
        homePageDto.setIsPayCount(isPayCount);
        homePageDto.setEntUserAddCountPerDay(entUserAddCountPerDay);
        return homePageDto;
    }

    @Override
    public void synchroHistData() {
        // ???????????????????????????????????????????????????entUserId??????,????????????????????????????????????
        List<EntUser> entUsers = mongoTemplate.find(new Query(where("entUserId").is(null)), EntUser.class);
        List<User> userList = new ArrayList<>();
        User user = null;
        if (!CollectionUtils.isEmpty(entUsers)) {
            for (EntUser entUser : entUsers) {
                String userId = commonUtils.getNumCode();
                user = new User();
                BeanUtils.copyProperties(entUser, user);
                user.setIsDelete(0);
                user.setIsVerify(false);
                user.setRoleCode(RoleCode.VISITOR.getCode());
                user.setUserId(userId);
                if (null == user.getCreateTime()) {
                    user.setCreateTime(new Date());
                }
                userList.add(user);

                Update update = new Update();
                update.set("entUserId", userId);
                mongoTemplate.updateFirst(query(where("openId").is(entUser.getOpenId())), update, EntUser.class);

                // ??????openId???????????????Project???entUserId???????????????????????????PC????????????entUserId??????????????????
                Update projectUpdate = new Update();
                projectUpdate.set("entUserId", userId);
                mongoTemplate.updateMulti(query(where("openId").is(entUser.getOpenId()).and("isDone").is(true)), update, Project.class);
            }
            mongoTemplate.insert(userList, User.class);
        }
    }

    @Override
    public Integer entUserCount(EntUserPageListDto entUserPageListDto) {
        Criteria criteria = new Criteria();
        List<String> roleCodeList = new ArrayList<>();
        roleCodeList.add("visitor");
        roleCodeList.add("entuser");
        criteria.and("roleCode").in(roleCodeList);
        if (!StringUtils.isEmpty(entUserPageListDto.getSearchField())) {
            criteria.orOperator(Criteria.where("phoneNm").is(entUserPageListDto.getSearchField()), Criteria.where("project.projectNm").regex(".*?\\" + entUserPageListDto.getSearchField() + ".*"));
        }
        LookupOperation lookup = LookupOperation.newLookup()
                //????????????????????????
                .from("project")
                //????????????????????????????????????
                .localField("userId")
                //?????????????????????????????????
                .foreignField("entUserId")
                //???????????????????????? ??????
                .as("project");
        AggregationOperation operation = Aggregation.match(criteria);
        Aggregation agg = Aggregation.newAggregation(lookup, operation);
        AggregationResults<Map> studentAggregation = mongoTemplate.aggregate(agg, "user", Map.class);
        int count = studentAggregation.getMappedResults().size();
        return count;
    }

    @Override
    public List<EntUserDto> entUserPageList(EntUserPageListDto entUserPageListDto) {
        int pageNum = entUserPageListDto.getPageNum();
        int pageSize = entUserPageListDto.getPageSize();
        String searchField = entUserPageListDto.getSearchField();
        int startNum = pageNum * pageSize;
        Criteria criteria = new Criteria();
        List<String> roleCodeList = new ArrayList<>();
        roleCodeList.add("visitor");
        roleCodeList.add("entuser");
        criteria.and("roleCode").in(roleCodeList);
        if (!StringUtils.isEmpty(searchField)) {
            criteria.orOperator(Criteria.where("phoneNm").is(searchField), Criteria.where("project.projectNm").regex(".*?\\" + searchField + ".*"));
        }
        LookupOperation lookup = LookupOperation.newLookup()
                //????????????????????????
                .from("project")
                //????????????????????????????????????
                .localField("userId")
                //?????????????????????????????????
                .foreignField("entUserId")
                //???????????????????????? ??????
                .as("project");
        AggregationOperation operation = Aggregation.match(criteria);

        Aggregation agg = Aggregation.newAggregation(lookup, operation,
                Aggregation.skip((long) startNum),
                Aggregation.limit(pageSize));
        AggregationResults<EntUserDto> result = mongoTemplate.aggregate(agg,"user", EntUserDto.class);
        List<EntUserDto> entUserDtos = result.getMappedResults();
        return entUserDtos;
    }

    @Override
    public void cancelVerify(String userId) {
        User user = mongoTemplate.findOne(query(where("userId").is(userId)), User.class);
        if (null == user || StringUtils.isEmpty(user.getRoleCode())) {
            return;
        }
        if (RoleCode.ENTUSER.getCode().equals(user.getRoleCode())) {
            // ????????????????????????
            mongoTemplate.remove(query(where("entUserId").is(user.getUserId())));
        } else if (RoleCode.INVESTOR.getCode().equals(user.getRoleCode())) {
            // ???????????????????????????
            mongoTemplate.remove(query(where("investorId").is(user.getUserId())), Investor.class);
        }

        // ????????????????????????????????????
        Update update = new Update();
        update.set("isVerify", false);
        update.set("roleCode", RoleCode.VISITOR.getCode());
        mongoTemplate.updateFirst(query(where("userId").is(user.getUserId())), update, User.class);
    }

    @Override
    public void editInvestor(InvestorEditDto investorEditDto) {
        User user = mongoTemplate.findOne(query(where("userId").is(investorEditDto.getUserId())), User.class);
        if (null == user) {
            return;
        }

    }

    /**
     * ???????????? ????????????????????????????????????
     * @param user
     */
    @Override
    @Async
    public void sendAuditMail(User user) throws Exception {
        SendEmailDto sendEmailDto = new SendEmailDto();
        sendEmailDto.setReceiver(adviserEmail);
        String roleName = RoleCode.getMessage(user.getRoleCode());
        sendEmailDto.setTheme(roleName + "????????????");
        StringBuilder stringBuilder = new StringBuilder("???????????????");
        stringBuilder.append("\n");
        stringBuilder.append("?????????").append(null == user.getUserName()?"":user.getUserName()).append("\n");
        stringBuilder.append("?????????").append(roleName).append("\n");
        stringBuilder.append("?????????").append(null == user.getCompanyName()?"":user.getCompanyName()).append("\n");
        stringBuilder.append("?????????").append(null == user.getPhoneNm()?"":user.getPhoneNm()).append("\n");
        stringBuilder.append("???????????????").append(null == user.getTelephoneNo()?"":user.getTelephoneNo()).append("\n");
        stringBuilder.append("\n");
        stringBuilder.append("??????????????????????????????????????????zrfintech-prd.srv.cmbchina.biz/management/index.html");
        sendEmailDto.setContent(stringBuilder.toString());
        emailService.sendSimpleTextMail(sendEmailDto);
    }

    @Override
    public PageListDto<User> pageList(PageDto pageDto) {
        int pageNum = pageDto.getPageNum();
        int pageSize = pageDto.getPageSize();

        Query query = new Query();
        if (!StringUtils.isEmpty(pageDto.getRoleCode())) {
            query.addCriteria(where("roleCode").is(pageDto.getRoleCode()));
        }
        int count = (int) mongoTemplate.count(query, Attention.class);
        int totalPage = count/pageSize;
        PageListDto pageListDto = new PageListDto<User>();
        pageListDto.setTotal(count);
        if(pageNum <= totalPage){
            int startNum = pageNum * pageSize;
            List<User> users = mongoTemplate.find(query.skip(startNum).limit(pageSize), User.class);
            pageListDto.setList(users);
        }
        return pageListDto;
    }

    @Override
    public void auditUser(String userId, Integer auditStatus) {
        User user = mongoTemplate.findOne(query(where("userId").is(userId)), User.class);
        Update update = new Update();
        update.set("auditStatus", auditStatus);
        if (2 == auditStatus) {
            update.set("isVerify", true);
            if (RoleCode.INVESTOR.getCode().equals(user.getRoleCode())) {
                Update investorUpdate = new Update();
                investorUpdate.set("status", 0);
                mongoTemplate.updateFirst(query(where("investorId").is(user.getUserId())), investorUpdate, Investor.class);
            } else if (RoleCode.FINANCIALADVISOR.getCode().equals(user.getRoleCode())) {
                Update faUpdate = new Update();
                faUpdate.set("status", 0);
                mongoTemplate.updateFirst(query(where("faId").is(user.getUserId())), faUpdate, FinancialAdvisor.class);
            }
        }
        update.set("updateTime", new Date());
        mongoTemplate.updateFirst(query(where("userId").is(userId)), update, User.class);
    }
}
