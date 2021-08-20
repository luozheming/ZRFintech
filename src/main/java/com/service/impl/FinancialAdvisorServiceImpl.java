package com.service.impl;

import com.dto.indto.PageDto;
import com.dto.outdto.PageListDto;
import com.pojo.FinancialAdvisor;
import com.pojo.Investor;
import com.pojo.Project;
import com.service.FinancialAdvisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class FinancialAdvisorServiceImpl implements FinancialAdvisorService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public PageListDto<FinancialAdvisor> pageList(PageDto pageDto) {
        int pageNum = pageDto.getPageNum();
        int pageSize = pageDto.getPageSize();

        Query query = new Query();
        query.addCriteria(where("status").is(0));
        if (null != pageDto.getFaType()) {
            query.addCriteria(where("faType").is(pageDto.getFaType()));
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
}
