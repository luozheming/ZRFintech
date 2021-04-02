package com.service.manage;

import com.pojo.Investor;
import com.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
public class InvestorService {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    CommonUtils commonUtils;

    public List<Investor> getInvestor(int pageNum,int pageSize){
        int startNum = pageNum*pageSize;
        return mongoTemplate.find(new Query().skip(startNum).limit(pageSize), Investor.class);
    }

    public int getDataCount(){
        //长度截断
        return (int) mongoTemplate.count(new Query(),"investor");
    }

    public Investor getInvesById(String investorId){
        return mongoTemplate.findOne(query(where("investorId").is(investorId)),Investor.class);
    }

    public void addInvestor(Investor investor){
        mongoTemplate.save(investor);
    }

    public void editInvestor(Investor investor){
        mongoTemplate.remove(query(where("investId").is(investor.getInvestorId())),Investor.class);
        mongoTemplate.save(investor);
    }

    public void status(String investorId, Integer status){
        Update update = new Update();
        update.addToSet("status", status);
        mongoTemplate.updateFirst(query(Criteria.where("investorId").is(investorId)), update, Investor.class);
    }
}
