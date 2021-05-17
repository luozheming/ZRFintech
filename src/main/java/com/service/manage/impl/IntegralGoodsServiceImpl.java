package com.service.manage.impl;

import com.pojo.IntegralGoods;
import com.service.manage.IntegralGoodsService;
import com.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class IntegralGoodsServiceImpl implements IntegralGoodsService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CommonUtils commonUtils;

    @Override
    public List<IntegralGoods> pageList(Integer pageNum, Integer pageSize, BigDecimal integralStart, BigDecimal integralEnd) {
        Criteria criteria = new Criteria();
        if (null != integralStart && null != integralEnd) {
            criteria = where("integral").gte(integralStart).and("integral").lt(integralEnd);
        }
        int startNum = pageNum * pageSize;
        List<IntegralGoods> integralGoods = mongoTemplate.find(new Query(criteria).skip(startNum).limit(pageSize), IntegralGoods.class);
        if (!CollectionUtils.isEmpty(integralGoods)) {
            for (IntegralGoods goods: integralGoods) {
                String photoRoute = goods.getPhotoRoute();
                // 获取商品图片信息
                goods.setPhoto(commonUtils.getPhoto(photoRoute));
            }
        }
        return integralGoods;
    }

    @Override
    public Integer count() {
        return (int) mongoTemplate.count(new Query(),"integralGoods");
    }

    @Override
    public IntegralGoods detail(String id) {
        IntegralGoods integralGoods = mongoTemplate.findOne(query(where("id").is(id)), IntegralGoods.class);
        if (null != integralGoods && StringUtils.isEmpty(integralGoods.getPhotoRoute())) {
            integralGoods.setPhoto(commonUtils.getPhoto(integralGoods.getPhotoRoute()));
        }
        return integralGoods;
    }

    @Override
    public void add(IntegralGoods integralGoods) {
        integralGoods.setId(commonUtils.getNumCode());
        mongoTemplate.save(integralGoods);
    }

    @Override
    public void edit(IntegralGoods integralGoods) {
        Update update = new Update();
        if (!StringUtils.isEmpty(integralGoods.getPhotoRoute())) {
            update.set("photoRoute", integralGoods.getPhotoRoute());
        }
        update.set("goodsName", integralGoods.getGoodsName());
        update.set("integral", integralGoods.getIntegral());
        update.set("goodsDesc", integralGoods.getGoodsDesc());
        mongoTemplate.updateFirst(query(where("id").is(integralGoods.getId())), update, IntegralGoods.class);
    }
}
