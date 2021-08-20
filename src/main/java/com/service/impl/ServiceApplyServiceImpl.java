package com.service.impl;

import com.dto.indto.PageDto;
import com.dto.outdto.PageListDto;
import com.pojo.ServiceApply;
import com.pojo.Investor;
import com.pojo.User;
import com.service.ServiceApplyService;
import com.utils.CommonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class ServiceApplyServiceImpl implements ServiceApplyService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CommonUtils commonUtils;

    @Override
    public PageListDto<ServiceApply> pageList(PageDto pageDto) {
        int pageNum = pageDto.getPageNum();
        int pageSize = pageDto.getPageSize();

        Query query = new Query();
        int count = (int) mongoTemplate.count(query, ServiceApply.class);

        int totalPage = count/pageSize;
        PageListDto pageListDto = new PageListDto<Investor>();
        pageListDto.setTotal(count);
        if(pageNum <= totalPage){
            int startNum = pageNum * pageSize;
            List<ServiceApply> serviceApplys = mongoTemplate.find(query.skip(startNum).limit(pageSize), ServiceApply.class);
            pageListDto.setList(serviceApplys);
        }
        return pageListDto;
    }

    @Override
    public void add(ServiceApply serviceApply) {
        User user = mongoTemplate.findOne(query(where("userId").is(serviceApply.getUserId())), User.class);
        if (null == user) {
            return;
        }
        String phoneNm = serviceApply.getPhoneNm();
        BeanUtils.copyProperties(user, serviceApply);
        serviceApply.setId(commonUtils.getNumCode());
        if (!StringUtils.isEmpty(phoneNm)) {
            serviceApply.setPhoneNm(phoneNm);
        }
        serviceApply.setStatus(0);
        serviceApply.setCreateTime(new Date());
        mongoTemplate.insert(serviceApply, "serviceApply");
    }
}
