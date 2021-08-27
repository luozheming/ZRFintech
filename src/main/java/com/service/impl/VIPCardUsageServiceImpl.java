package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.VIPCardUsageRespDto;
import com.pojo.*;
import com.service.VIPCardUsageLogService;
import com.service.VIPCardUsageService;
import com.utils.CommonUtils;
import com.utils.DateUtil;
import com.utils.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class VIPCardUsageServiceImpl implements VIPCardUsageService {

    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private VIPCardUsageLogService vipCardUsageLogService;
    private static final int proDeliverTimesMonth = 200;// 系统默认200次
    private static final int proDeliverTimesDay = 3;// 系统默认每天免费3次

    private static final Logger logger = LoggerFactory.getLogger(VIPCardUsageServiceImpl.class);

    @Override
    public VIPCardUsageRespDto detailByUserId(String userId) {
        logger.info("会员卡详情入参：userId=" + userId);
        VIPCardUsageRespDto vipCardUsageRespDto = new VIPCardUsageRespDto();
        VIPCardUsage vipCardUsage = mongoTemplate.findOne(query(where("userId").is(userId)), VIPCardUsage.class);
        if (null != vipCardUsage) {
            if (DateUtil.getDiffDate(vipCardUsage.getEndTime(), new Date(), 1) > 0) {
                vipCardUsageRespDto.setIsValid(false);
            } else {
                BeanUtils.copyProperties(vipCardUsage, vipCardUsageRespDto);
                // 获取会员卡的服务使用记录
                List<VIPCardUsageLog> usageLogs = vipCardUsageLogService.list(vipCardUsage.getId());
                int proDeliverTimesPerMonth = 0;// 当月项目群发服务已使用次数
                if (!CollectionUtils.isEmpty(usageLogs)) {
                    for (VIPCardUsageLog vipCardUsageLog : usageLogs) {
                        // 获取当月的服务使用情况
                        int diff = DateUtil.getDiffDate(vipCardUsageLog.getCreateTime(), new Date(), 5);
                        if (diff == 0) {
                            // vip服务类型：1-项目群发
                            if (1 == vipCardUsageLog.getVipServiceType()) {
                                proDeliverTimesPerMonth = vipCardUsageLog.getUsageTimes() + proDeliverTimesPerMonth;
                            }
                        }
                    }
                }
                vipCardUsageRespDto.setProDeliverTimesPerMonth(proDeliverTimesMonth - proDeliverTimesPerMonth);
                vipCardUsageRespDto.setIsValid(true);
            }
        }

        // 查看用户当天使用单个bp投递情况
        String date = DateUtil.dateToStr(new Date(), "yyyy-MM-dd");
        Date dateStart = DateUtil.strToDate(date + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date dateEnd = DateUtil.strToDate(date + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        List<ProjectDeliver> projectDelivers = mongoTemplate.find(query(where("userId").is(userId).and("createDate").gt(dateStart).lt(dateEnd).and("vipCardUsageLogId").is(null)), ProjectDeliver.class);
        vipCardUsageRespDto.setProDeliverTimesPerDay(proDeliverTimesDay);
        if (!CollectionUtils.isEmpty(projectDelivers)) {
            vipCardUsageRespDto.setProDeliverTimesPerDay(proDeliverTimesDay - projectDelivers.size());
        }
        logger.info("会员卡详情返回：vipCardUsageRespDto=" + JSONObject.toJSONString(vipCardUsageRespDto));
        return vipCardUsageRespDto;
    }

    @Override
    public void add(VIPCardUsage vipCardUsage) throws Exception {
        // 通过vip卡的id获取详细信息
        logger.info(vipCardUsage.getVipCardId());
        VIPCard vipCard = mongoTemplate.findOne(query(where("id").is(vipCardUsage.getVipCardId())), VIPCard.class);
        if (null == vipCard) {
            logger.error("查询会员卡信息为空");
            throw new Exception(ErrorCode.NULLOBJECT.getMessage());
        }

        VIPCardUsage vipCardUsageResp = mongoTemplate.findOne(query(where("userId").is(vipCardUsage.getUserId())), VIPCardUsage.class);
        if (null != vipCardUsageResp) {
            Date updateEndTime = null;
            if (null != vipCardUsageResp.getEndTime()) {
                switch (vipCard.getVipCardType()) {
                    case 1:
                        updateEndTime = DateUtil.getNextDate(vipCardUsageResp.getEndTime(), 5, 1);
                        break;
                    case 2:
                        updateEndTime = DateUtil.getNextDate(vipCardUsageResp.getEndTime(), 5, 3);
                        break;
                    case 3:
                        updateEndTime = DateUtil.getNextDate(vipCardUsageResp.getEndTime(), 6, 1);
                        break;
                    default:
                        updateEndTime = vipCardUsageResp.getEndTime();
                        break;
                }
            }
            Update update = new Update();
            update.set("endTime", updateEndTime);
            mongoTemplate.updateFirst(query(where("id").is(vipCardUsageResp.getId())), update, VIPCardUsage.class);
        } else {
            // 初始化vip卡使用情况数据
            VIPCardUsage vipCardUsageAdd = new VIPCardUsage();
            vipCardUsageAdd.setId(commonUtils.getNumCode());
            vipCardUsageAdd.setVipCardId(vipCard.getId());
            vipCardUsageAdd.setOpenId(vipCardUsage.getOpenId());
            vipCardUsageAdd.setUserId(vipCardUsage.getUserId());
            Date startTime = new Date();
            Date endTime = null;
            switch (vipCard.getVipCardType()) {
                case 1:
                    endTime = DateUtil.getNextDate(startTime, 5, 1);
                    break;
                case 2:
                    endTime = DateUtil.getNextDate(startTime, 5, 3);
                    break;
                case 3:
                    endTime = DateUtil.getNextDate(startTime, 6, 1);
                    break;
                default:
                    break;
            }
            vipCardUsageAdd.setStartTime(startTime);
            vipCardUsageAdd.setEndTime(endTime);
            vipCardUsageAdd.setVipTemplate(vipCardUsage.getVipTemplate());
            mongoTemplate.save(vipCardUsageAdd);
        }
    }

    @Override
    public void edit(VIPCardUsage vipCardUsage) {
        Update update = new Update();
        if (null != vipCardUsage.getVipTemplate()) {
            update.set("vipTemplate", vipCardUsage.getVipTemplate());
        }
        mongoTemplate.updateFirst(query(where("id").is(vipCardUsage.getId())), update, VIPCardUsage.class);
    }

}
