package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dto.indto.PageDto;
import com.dto.outdto.PageListDto;
import com.pojo.FinancialAdvisor;
import com.pojo.Investor;
import com.pojo.Project;
import com.service.FinancialAdvisorService;
import com.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class FinancialAdvisorServiceImpl implements FinancialAdvisorService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CommonUtils commonUtils;

    private static final Logger logger = LoggerFactory.getLogger(FinancialAdvisorServiceImpl.class);

    @Override
    public PageListDto<FinancialAdvisor> pageList(PageDto pageDto) {
        int pageNum = pageDto.getPageNum();
        int pageSize = pageDto.getPageSize();

        Query query = new Query();
        query.addCriteria(where("status").is(0));
        if (null != pageDto.getFaType()) {
            query.addCriteria(where("faType").is(pageDto.getFaType()));
        }
        if (null != pageDto.getShowFlag()) {
            query.addCriteria(where("showFlag").is(pageDto.getShowFlag()));
        }
        if (null != pageDto.getOrgFaId()) {
            query.addCriteria(where("orgFaId").is(pageDto.getOrgFaId()));
        }

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

        int count = (int) mongoTemplate.count(query, FinancialAdvisor.class);

        int totalPage = count/pageSize;
        PageListDto pageListDto = new PageListDto<Investor>();
        pageListDto.setTotal(count);
        if(pageNum <= totalPage){
            int startNum = pageNum * pageSize;
            List<FinancialAdvisor> financialAdvisors = mongoTemplate.find(query.skip(startNum).limit(pageSize), FinancialAdvisor.class);
            pageListDto.setList(financialAdvisors);
        }
        return pageListDto;
    }

    @Override
    public void add(FinancialAdvisor financialAdvisor) {
        financialAdvisor.setFaId(commonUtils.getNumCode());
        financialAdvisor.setStatus(0);
        financialAdvisor.setCreateTime(new Date());
        financialAdvisor.setSourceDesc("系统录入");
        mongoTemplate.insert(financialAdvisor, "financialAdvisor");
    }

    @Override
    public void edit(FinancialAdvisor financialAdvisor) {
        logger.info("修改FA资料入参：" + JSONObject.toJSONString(financialAdvisor));
        Update update = new Update();
        if (!StringUtils.isEmpty(financialAdvisor.getOrgNm())) {
            update.set("orgNm", financialAdvisor.getOrgNm());
        }
        if (!StringUtils.isEmpty(financialAdvisor.getPositionName())) {
            update.set("positionName", financialAdvisor.getPositionName());
        }
        if (!StringUtils.isEmpty(financialAdvisor.getPhoneNm())) {
            update.set("phoneNm", financialAdvisor.getPhoneNm());
        }
        if (!StringUtils.isEmpty(financialAdvisor.getFaName())) {
            update.set("faName", financialAdvisor.getFaName());
        }
        if (!StringUtils.isEmpty(financialAdvisor.getIntrod())) {
            update.set("introd", financialAdvisor.getIntrod());
        }
        if (!StringUtils.isEmpty(financialAdvisor.getSelfIntroduction())) {
            update.set("selfIntroduction", financialAdvisor.getSelfIntroduction());
        }
        if (!StringUtils.isEmpty(financialAdvisor.getContactUser())) {
            update.set("contactUser", financialAdvisor.getContactUser());
        }
        if (!StringUtils.isEmpty(financialAdvisor.getTelephoneNo())) {
            update.set("telephoneNo", financialAdvisor.getTelephoneNo());
        }
        if (!StringUtils.isEmpty(financialAdvisor.getEmail())) {
            update.set("email", financialAdvisor.getEmail());
        }
        if (!StringUtils.isEmpty(financialAdvisor.getPhotoRoute())) {
            update.set("photoRoute", financialAdvisor.getPhotoRoute());
        }
        if (!StringUtils.isEmpty(financialAdvisor.getFocusFiled())) {
            update.set("focusFiled", financialAdvisor.getFocusFiled());
        }
        if (!StringUtils.isEmpty(financialAdvisor.getFinRound())) {
            update.set("finRound", financialAdvisor.getFinRound());
        }
        if (!StringUtils.isEmpty(financialAdvisor.getCity())) {
            update.set("city", financialAdvisor.getCity());
        }
        if (!StringUtils.isEmpty(financialAdvisor.getInvestmentCase())) {
            update.set("investmentCase", financialAdvisor.getInvestmentCase());
        }
        if (!StringUtils.isEmpty(financialAdvisor.getShowFlag())) {
            update.set("showFlag", financialAdvisor.getShowFlag());
        }
        mongoTemplate.updateFirst(query(where("faId").is(financialAdvisor.getFaId())), update, FinancialAdvisor.class);
    }

    @Override
    public void status(FinancialAdvisor financialAdvisor) {
        Update update = new Update();
        if (null != financialAdvisor.getStatus()) {
            update.set("status", financialAdvisor.getStatus());
        }
        mongoTemplate.updateFirst(query(where("faId").is(financialAdvisor.getFaId())), update, FinancialAdvisor.class);
    }

    @Override
    public void delete(String faId) {
        mongoTemplate.remove(query(where("faId").is(faId)), FinancialAdvisor.class);
    }
}
