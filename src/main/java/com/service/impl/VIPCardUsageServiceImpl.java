package com.service.impl;

import com.dto.indto.PurchaseVIPCardDto;
import com.pojo.VIPCard;
import com.pojo.VIPCardUsage;
import com.service.VIPCardUsageService;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
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
    public void purchaseVIPCard(PurchaseVIPCardDto purchaseVIPCardDto) throws Exception {
        // 通过vip卡的id获取详细信息
        List<VIPCard> vipCards = mongoTemplate.find(new Query(), VIPCard.class);
        VIPCard vipCard = new VIPCard();
        if (CollectionUtils.isEmpty(vipCards)) {
            throw new Exception(ErrorCode.NULLOBJECT.toJsonString());
        } else {
            vipCard = vipCards.get(0);
        }

        // 没有VIP卡的服务初始记录，则新增一笔
        VIPCardUsage vipCardUsage = mongoTemplate.findOne(query(where("openId").is(purchaseVIPCardDto.getOpenId())), VIPCardUsage.class);
        if (null == vipCardUsage) {
            // 为空则初始化一笔vip卡片的可使用服务相关信息
            VIPCardUsage vipCardUsageAdd = new VIPCardUsage();
            vipCardUsageAdd.setId(commonUtils.getNumCode());
            vipCardUsageAdd.setCardId(vipCard.getId());
            vipCardUsageAdd.setOpenId(purchaseVIPCardDto.getOpenId());
            vipCardUsageAdd.setCardCount(purchaseVIPCardDto.getCardCount());
            vipCardUsageAdd.setBpApplyTimes(vipCard.getBpApplyTimes() * purchaseVIPCardDto.getCardCount());
            vipCardUsageAdd.setCommentTimes(vipCard.getCommentTimes() * purchaseVIPCardDto.getCardCount());
            mongoTemplate.save(vipCardUsageAdd);
        }/* else {
            // 不为空，则更新对于vip卡片的可使用服务的次数
            Update update = new Update();
            update.set("bpApplyTimes", vipCardUsage.getBpApplyTimes() + vipCard.getBpApplyTimes() * purchaseVIPCardDto.getCardCount());
            update.set("commentTimes", vipCardUsage.getCommentTimes() + vipCard.getBpApplyTimes() * purchaseVIPCardDto.getCardCount());
            mongoTemplate.updateFirst(query(where("openId").is(purchaseVIPCardDto.getOpenId())), update, VIPCardUsage.class);
        }*/
    }
}
