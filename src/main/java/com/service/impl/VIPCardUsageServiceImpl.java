package com.service.impl;

import com.pojo.VIPCard;
import com.pojo.VIPCardUsage;
import com.service.VIPCardUsageService;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class VIPCardUsageServiceImpl implements VIPCardUsageService {

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public VIPCardUsage detailByEnt(String openId) {
        return mongoTemplate.findOne(query(where("openId").is(openId)), VIPCardUsage.class);
    }

    @Override
    public void add(VIPCardUsage vipCardUsage) throws Exception {
        // 通过vip卡的id获取详细信息
        List<VIPCard> vipCards = mongoTemplate.find(new Query(), VIPCard.class);
        VIPCard vipCard = new VIPCard();
        if (CollectionUtils.isEmpty(vipCards)) {
            throw new Exception(ErrorCode.NULLOBJECT.toJsonString());
        } else {
            vipCard = vipCards.get(0);
        }

        VIPCardUsage vipCardUsageResp = mongoTemplate.findOne(query(where("openId").is(vipCardUsage.getOpenId())), VIPCardUsage.class);
        if (null == vipCardUsageResp) {
            // 初始化vip卡使用情况数据
            VIPCardUsage vipCardUsageAdd = new VIPCardUsage();
            vipCardUsageAdd.setId(commonUtils.getNumCode());
            vipCardUsageAdd.setCardId(vipCard.getId());
            vipCardUsageAdd.setOpenId(vipCardUsage.getOpenId());
            vipCardUsageAdd.setCardCount(vipCardUsage.getCardCount());
            vipCardUsageAdd.setBpApplyTimes(vipCard.getBpApplyTimes() * vipCardUsage.getCardCount());
            vipCardUsageAdd.setCommentTimes(vipCard.getCommentTimes() * vipCardUsage.getCardCount());
            mongoTemplate.save(vipCardUsageAdd);
        } else {
            throw new Exception(ErrorCode.ONECECARDALREADY.toJsonString());
        }
    }

    @Override
    public void edit(VIPCardUsage vipCardUsage) {
        Update update = new Update();
        update.set("bpApplyTimes", vipCardUsage.getBpApplyTimes());
        update.set("commentTimes", vipCardUsage.getCommentTimes());
        mongoTemplate.updateFirst(query(where("id").is(vipCardUsage.getId())), update, VIPCardUsage.class);
    }

    @Override
    public void clearTimes(String openId) {
        Update update = new Update();
        update.set("bpApplyTimes", 0);
        update.set("commentTimes", 0);
        mongoTemplate.updateFirst(query(where("openId").is(openId)), update, VIPCardUsage.class);
    }
}
