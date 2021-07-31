package com.service.manage.impl;

import com.dto.outdto.EntUserDto;
import com.service.manage.EntUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class EntUserServiceImpl implements EntUserService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<EntUserDto> pageList(Integer pageNum, Integer pageSize, String searchField) throws Exception {
        int startNum = pageNum * pageSize;
        Criteria criteria = new Criteria();
        criteria.and("entUserId").ne(null);
        if (!StringUtils.isEmpty(searchField)) {
            criteria.orOperator(Criteria.where("phoneNm").is(searchField), Criteria.where("project.projectNm").regex(".*?\\" + searchField + ".*"));
        }
        LookupOperation lookup = LookupOperation.newLookup()
                //从表（关联的表）
                .from("project")
                //主表中与从表相关联的字段
                .localField("entUserId")
                //从表与主表相关联的字段
                .foreignField("entUserId")
                //查询出的从表集合 命名
                .as("project");
        AggregationOperation operation = Aggregation.match(criteria);

        Aggregation agg = Aggregation.newAggregation(lookup, operation,
                Aggregation.skip((long) startNum),
                Aggregation.limit(pageSize));
        AggregationResults<EntUserDto> result = mongoTemplate.aggregate(agg,"entuser", EntUserDto.class);
        List<EntUserDto> entUserDtos = result.getMappedResults();
        return entUserDtos;
    }
    
    @Override
    public Integer count(String searchField) {
        Criteria criteria = new Criteria();
        criteria.and("entUserId").ne(null);
        if (!StringUtils.isEmpty(searchField)) {
            criteria.orOperator(Criteria.where("phoneNm").is(searchField), Criteria.where("project.projectNm").regex(".*?\\" + searchField + ".*"));
        }
        LookupOperation lookup = LookupOperation.newLookup()
                //从表（关联的表）
                .from("project")
                //主表中与从表相关联的字段
                .localField("entUserId")
                //从表与主表相关联的字段
                .foreignField("entUserId")
                //查询出的从表集合 命名
                .as("project");
        AggregationOperation operation = Aggregation.match(criteria);
        Aggregation agg = Aggregation.newAggregation(lookup, operation);
        AggregationResults<Map> studentAggregation = mongoTemplate.aggregate(agg, "entuser", Map.class);
        int count = studentAggregation.getMappedResults().size();
        return count;
    }
}
