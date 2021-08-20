package com.controller.manage;

import com.pojo.Investor;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * 同步历史数据：用于各个版本迭代引起的必要表数据的迁移或者变更
 */
@RestController
@RequestMapping("/synchroHistData")
public class SynchroHistDataController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/changeInvestorPhoto")
    public String changeInvestorPhoto() {
        int n = 13;
        String defaultPhotoRoute = "/home/ec2-user/data/investor/默认投资人.jpg";
        String photoRoute = "";
        String fileName = "";
        List<Investor> investorList = mongoTemplate.find(new Query(), Investor.class);
        if (!CollectionUtils.isEmpty(investorList)) {
            Update update = null;
            for (Investor investor : investorList) {
                if (!defaultPhotoRoute.equals(investor.getInvesPhotoRoute())) {
                    continue;
                }
                photoRoute = "/home/ec2-user/data/investor/";
                int random = new Random().nextInt(n) + 1;
                fileName = random + ".jpg";
                photoRoute = photoRoute + fileName;
                update = new Update();
                update.set("invesPhotoRoute", photoRoute);
                update.set("", "");
                mongoTemplate.updateFirst(query(where("investorId").is(investor.getInvestorId())), update, Investor.class);
            }
        }
        return ErrorCode.SUCCESS.toJsonString();
    }
}
