package com.service.impl;

import com.dto.indto.PageDto;
import com.dto.outdto.AttentionOutDto;
import com.dto.outdto.PageListDto;
import com.enums.RoleCode;
import com.pojo.*;
import com.service.AttentionService;
import com.utils.CommonUtils;
import com.utils.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class AttentionServiceImpl implements AttentionService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CommonUtils commonUtils;

    @Override
    public PageListDto<Attention> pageList(PageDto pageDto) {
        int pageNum = pageDto.getPageNum();
        int pageSize = pageDto.getPageSize();

        Query query = new Query();
        query.addCriteria(where("userId").is(pageDto.getUserId()));
        if (!StringUtils.isEmpty(pageDto.getRoleCode())) {
            query.addCriteria(where("attentionRoleCode").is(pageDto.getRoleCode()));
        }
        if (!StringUtils.isEmpty(pageDto.getFaType())) {
            query.addCriteria(where("attentionFaType").is(pageDto.getFaType()));
        }
        int count = (int) mongoTemplate.count(query, Attention.class);

        int totalPage = count/pageSize;
        PageListDto pageListDto = new PageListDto<AttentionOutDto>();
        pageListDto.setTotal(count);
        if(pageNum <= totalPage){
            List<AttentionOutDto> attentionOutDtoList = new ArrayList<>();
            AttentionOutDto attentionOutDto = null;
            int startNum = pageNum * pageSize;
            List<Attention> attentions = mongoTemplate.find(query.skip(startNum).limit(pageSize), Attention.class);
            if (!CollectionUtils.isEmpty(attentions)) {
                for (Attention attention : attentions) {
                    attentionOutDto = new AttentionOutDto();
                    BeanUtils.copyProperties(attention, attentionOutDto);
                    // 判断是否为会员
                    VIPCardUsage vipCardUsage = mongoTemplate.findOne(query(where("userId").is(attention.getAttentionUserId())), VIPCardUsage.class);
                    if (null != vipCardUsage && DateUtil.getDiffDate(new Date(), vipCardUsage.getEndTime(), 1) > 0) {
                        attentionOutDto.setIsValid(true);
                    } else {
                        attentionOutDto.setIsValid(false);
                    }

                    /**
                     * 判断是否已认证
                     */
                    User userResp = mongoTemplate.findOne(query(where("userId").is(attention.getAttentionUserId())), User.class);
                    Boolean isVerify = false;
                    if (null != userResp && userResp.getIsVerify()) {
                        isVerify = true;
                    }

                    /**
                     * 被关注者对象信息
                     */
                    if (RoleCode.ENTUSER.getCode().equals(attention.getAttentionRoleCode())) {
                        User user = mongoTemplate.findOne(query(where("userId").is(attention.getAttentionUserId())), User.class);
                        attentionOutDto.setAttentionObject(user);
                    } else if (RoleCode.INVESTOR.getCode().equals(attention.getAttentionRoleCode())) {
                        Investor investor = mongoTemplate.findOne(query(where("investorId").is(attention.getAttentionUserId())), Investor.class);
                        investor.setIsVerify(isVerify);
                        attentionOutDto.setAttentionObject(investor);
                    } else if (RoleCode.FINANCIALADVISOR.getCode().equals(attention.getAttentionRoleCode())) {
                        FinancialAdvisor financialAdvisor = mongoTemplate.findOne(query(where("faId").is(attention.getAttentionUserId())), FinancialAdvisor.class);
                        attentionOutDto.setAttentionObject(financialAdvisor);
                    }
                    attentionOutDtoList.add(attentionOutDto);
                }
            }
            pageListDto.setList(attentionOutDtoList);
        }
        return pageListDto;
    }

    @Override
    public void add(Attention attention) {
        Attention attentionAdd = new Attention();
        User user = null;
        Investor investor = null;
        FinancialAdvisor financialAdvisor = null;
        if (!StringUtils.isEmpty(attention.getProjectNo())) {
            Project project = mongoTemplate.findOne(query(where("projectNo").is(attention.getProjectNo()).and("isDone").is(true)), Project.class);
            if (null == project) {
                return;
            }
            user = mongoTemplate.findOne(query(where("userId").is(project.getEntUserId())), User.class);
        }

        if (user == null && RoleCode.INVESTOR.getCode().equals(attention.getAttentionRoleCode())) {
//            user = mongoTemplate.findOne(query(where("userId").is(attention.getAttentionUserId())), User.class);
//            if (null == user) {
//                investor = mongoTemplate.findOne(query(where("investorId").is(attention.getAttentionUserId())), Investor.class);
//            }
            investor = mongoTemplate.findOne(query(where("investorId").is(attention.getAttentionUserId())), Investor.class);
        }

        if (user == null && RoleCode.FINANCIALADVISOR.getCode().equals(attention.getAttentionRoleCode())) {
//            user = mongoTemplate.findOne(query(where("userId").is(attention.getAttentionUserId())), User.class);
//            if (null == user) {
//                financialAdvisor = mongoTemplate.findOne(query(where("faId").is(attention.getAttentionUserId())), FinancialAdvisor.class);
//            }
            financialAdvisor = mongoTemplate.findOne(query(where("faId").is(attention.getAttentionUserId())), FinancialAdvisor.class);
        }

        BeanUtils.copyProperties(attention, attentionAdd);
        attentionAdd.setId(commonUtils.getNumCode());
        attentionAdd.setCreateTime(new Date());
        if (null == user && null == investor && null == financialAdvisor) {
            return;
        } else if (null != user) {
            attentionAdd.setAttentionUserId(user.getUserId());
            attentionAdd.setAttentionUserName(user.getUserName());
            attentionAdd.setAttentionCompanyName(user.getCompanyName());
            attentionAdd.setAttentionPositionName(user.getPositionName());
            attentionAdd.setAttentionRoleCode(user.getRoleCode());
            attentionAdd.setAttentionPhotoRoute(user.getPhotoRoute());
        } else if (null != investor) {
            attentionAdd.setAttentionUserId(investor.getInvestorId());
            attentionAdd.setAttentionUserName(investor.getInvestor());
            attentionAdd.setAttentionCompanyName(investor.getOrgNm().split("\\|")[0]);
            attentionAdd.setAttentionPositionName(investor.getOrgNm().split("\\|")[1]);
            attentionAdd.setAttentionPhotoRoute(investor.getInvesPhotoRoute());
        } else if (null != financialAdvisor) {
            attentionAdd.setAttentionUserId(financialAdvisor.getFaId());
            attentionAdd.setAttentionUserName(financialAdvisor.getFaName());
            attentionAdd.setAttentionCompanyName(financialAdvisor.getOrgNm());
            attentionAdd.setAttentionPositionName(financialAdvisor.getPositionName());
            attentionAdd.setAttentionPhotoRoute(financialAdvisor.getPhotoRoute());
            attentionAdd.setAttentionFaType(financialAdvisor.getFaType());
        }

        // 查询是否有重复关注情况
        Attention attentionResp = mongoTemplate.findOne(query(where("userId").is(attentionAdd.getUserId()).and("attentionUserId").is(attentionAdd.getAttentionUserId())), Attention.class);
        if (null != attentionResp) {
            return;
        }
        mongoTemplate.insert(attentionAdd, "attention");
    }

    @Override
    public void cancel(Attention attention) {
        mongoTemplate.remove(query(where("userId").is(attention.getUserId()).and("attentionUserId").is(attention.getAttentionUserId())), Attention.class);
    }
}
