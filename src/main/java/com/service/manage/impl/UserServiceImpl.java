package com.service.manage.impl;

import com.dto.outdto.HomePageDto;
import com.enums.OrderBizType;
import com.pojo.*;
import com.service.manage.UserService;
import com.utils.CommonUtils;
import com.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        // 所有的统计需排除内部人员操作的数据
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

//                    if (null != project.getProjectType() && (2 == project.getProjectType() || 3 == project.getProjectType())) {
//                        roadShowCount ++;
//                    }
                }
            }
        }

        // 查询当天用户增长数
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
        // 同步用户信息，给企业客户表新增字段entUserId赋值,且更新项目、项目评论信息
        List<EntUser> entUsers = mongoTemplate.find(new Query(where("entUserId").is(null)), EntUser.class);
        if (!CollectionUtils.isEmpty(entUsers)) {
            for (EntUser entUser : entUsers) {
                Update update = new Update();
                String entUserId = commonUtils.getNumCode();
                update.set("entUserId", entUserId);
                mongoTemplate.updateFirst(query(where("openId").is(entUser.getOpenId())), update, EntUser.class);

                // 通过openId更新项目表Project的entUserId，方便后续查询时跟PC端同步用entUserId查询相关信息
                Update projectUpdate = new Update();
                projectUpdate.set("entUserId", entUserId);
                mongoTemplate.updateMulti(query(where("openId").is(entUser.getOpenId())), update, Project.class);

                // 通过openId更新评论表ProjectComment的entUserId，方便后续查询时跟PC端同步用entUserId查询相关信息
                Update commentUpdate = new Update();
                commentUpdate.set("entUserId", entUserId);
                mongoTemplate.updateMulti(query(where("openId").is(entUser.getOpenId())), update, ProjectComment.class);
            }
        }

        // 获取评论信息生成订单信息
        List<ProjectComment> projectComments = mongoTemplate.find(new Query(), ProjectComment.class);
        if (!CollectionUtils.isEmpty(projectComments)) {
            List<Order> orderList = new ArrayList<>();
            Order order = null;
            for (ProjectComment projectComment : projectComments) {
                order = new Order();
                order.setOrderNo(commonUtils.getNumCode().substring(0, 32));
                order.setBizId(projectComment.getId());
                Integer bizType = projectComment.getCommentType() == null ? 1 : projectComment.getCommentType();
                order.setBizType(bizType);// 详情见OrderBizType
                order.setOpenId(projectComment.getOpenId());
                Integer commentStatus = projectComment.getCommonStatus();
                if (!StringUtils.isEmpty(commentStatus) && 2 == commentStatus ) {
                    order.setBizStatus(11);
                } else {
                    order.setBizStatus(3);
                }
                order.setCreateTime(projectComment.getCreateTime());
                order.setPayAmount(projectComment.getCommentAmount());
                order.setStars(projectComment.getStars());
                order.setReply(projectComment.getReply());
                order.setReplyTm(projectComment.getReplyTm());
                orderList.add(order);
            }

            if (!CollectionUtils.isEmpty(orderList)) {
                mongoTemplate.insert(orderList, Order.class);
            }
        }

        // 同步商业计划书申请订单
        List<ProjectBpApply> projectBpApplyList = mongoTemplate.find(new Query(), ProjectBpApply.class);
        if (!CollectionUtils.isEmpty(projectBpApplyList)) {
            List<Order> orderList = new ArrayList<>();
            Order order = null;
            for(ProjectBpApply projectBpApply : projectBpApplyList) {
                order = new Order();
                order.setOrderNo(commonUtils.getNumCode().substring(0, 32));
                order.setBizId(projectBpApply.getId());
                order.setBizType(OrderBizType.BPAPPLAY.getCode());// 详情见OrderBizType.
                Integer commentStatus = projectBpApply.getDealStatus();
                if (!StringUtils.isEmpty(commentStatus) && 1 == commentStatus ) {
                    order.setBizStatus(11);
                } else {
                    order.setBizStatus(3);
                }
                order.setCreateTime(projectBpApply.getCreateTime());
                orderList.add(order);
            }

            if (!CollectionUtils.isEmpty(orderList)) {
                mongoTemplate.insert(orderList, Order.class);
            }
        }
    }

}
