package com.service.manage;

import com.pojo.Investor;
import com.pojo.ProjectComment;
import com.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
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
        return mongoTemplate.find(new Query().with(Sort.by(Sort.Order.desc("isPlatform"))).with(Sort.by(Sort.Order.desc("status"))).skip(startNum).limit(pageSize), Investor.class);
    }

    public int getDataCount(){
        //长度截断
        return (int) mongoTemplate.count(new Query(),"investor");
    }

    public Investor getInvesById(String investorId){
        Investor investor = mongoTemplate.findOne(query(where("investorId").is(investorId)),Investor.class);
        if (!StringUtils.isEmpty(investor.getInvesPhotoRoute())) {
            investor.setPhoto(commonUtils.getPhoto(investor.getInvesPhotoRoute()));
        }
        if (!StringUtils.isEmpty(investor.getInvesOrgPhotoRoute())) {
            investor.setOrgPhoto(commonUtils.getPhoto(investor.getInvesOrgPhotoRoute()));
        }
        if (!StringUtils.isEmpty(investor.getInvesCardRoute())) {
            investor.setCardPhoto(commonUtils.getPhoto(investor.getInvesCardRoute()));
        }
        // 通过investorId查询所有的评论信息
        List<ProjectComment> projectComments = mongoTemplate.find(query(where("investorId").is(investor).and("favor").ne(4)), ProjectComment.class);
        int accomplishedTimes = 0;
        int unAccomplishedTimes = 0;
        BigDecimal unaccomplishedAmount = new BigDecimal("0.00");
        BigDecimal stars = new BigDecimal("5.0");// 没有评论则默认5.0星
        if (!CollectionUtils.isEmpty(projectComments)) {
            stars = new BigDecimal("0");
            for (ProjectComment projectComment : projectComments) {
                if (projectComment.getIsDone()) {
                    accomplishedTimes++;
                } else {
                    unAccomplishedTimes++;
                    unaccomplishedAmount = unaccomplishedAmount.add(projectComment.getCommentAmount());
                }
                stars = stars.add(projectComment.getStars());
            }
            stars = stars.divide(new BigDecimal(projectComments.size())).setScale(1, BigDecimal.ROUND_HALF_UP);
        }
        investor.setActualStars(stars);
        if (null != investor.getInternalWeightingStars()) {
            stars = stars.add(investor.getInternalWeightingStars());
            if (new BigDecimal("5.0").compareTo(stars) > 1) {
                stars = new BigDecimal("5.0");// 实际评星+内部加权值不能大于5.0
            }
            investor.setStars(stars);
        }
        investor.setUnaccomplishedAmount(unaccomplishedAmount);// 未支付总额
        investor.setUnAccomplishedTimes(unAccomplishedTimes);// 未支付单数
        investor.setAccomplishedTimes(accomplishedTimes);// 已支付单数
        investor.setStars(stars);// 评星
        return investor;
    }

    public void addInvestor(Investor investor){
        mongoTemplate.save(investor);
    }

    public void editInvestor(Investor investor){
        mongoTemplate.remove(query(where("investorId").is(investor.getInvestorId())),Investor.class);
        mongoTemplate.save(investor);
    }

    public void status(String investorId, Integer status){
        Update update = new Update();
        update.set("status", status);
        mongoTemplate.updateFirst(query(Criteria.where("investorId").is(investorId)), update, Investor.class);
    }
}
