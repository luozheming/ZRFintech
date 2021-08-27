package com.service.impl;

import com.dto.indto.PageDto;
import com.dto.indto.SendEmailDto;
import com.dto.outdto.PageListDto;
import com.pojo.ServiceApply;
import com.pojo.Investor;
import com.pojo.User;
import com.service.EmailService;
import com.service.ServiceApplyService;
import com.utils.CommonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
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
    @Value("${platform.serviceApply.email}")
    private String serviceApplyEmail;
    @Autowired
    private EmailService emailService;

    @Override
    public PageListDto<ServiceApply> pageList(PageDto pageDto) {
        int pageNum = pageDto.getPageNum();
        int pageSize = pageDto.getPageSize();

        Query query = new Query();
        int count = (int) mongoTemplate.count(query, ServiceApply.class);

        int totalPage = count/pageSize;
        PageListDto pageListDto = new PageListDto<ServiceApply>();
        pageListDto.setTotal(count);
        if(pageNum <= totalPage){
            int startNum = pageNum * pageSize;
            List<ServiceApply> serviceApplys = mongoTemplate.find(query.skip(startNum).limit(pageSize), ServiceApply.class);
            pageListDto.setList(serviceApplys);
        }
        return pageListDto;
    }

    @Override
    public void add(ServiceApply serviceApply) throws Exception {
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

    /**
     * 发送邮件给顾问
     */
    @Async
    public void sendMail(ServiceApply serviceApply) throws Exception {
        SendEmailDto sendEmailDto = new SendEmailDto();
        sendEmailDto.setTheme(serviceApply.getServiceApplyName() + "申请");
        sendEmailDto.setReceiver(serviceApplyEmail);
        StringBuilder stringBuilder = new StringBuilder("您有一个" + serviceApply.getServiceApplyName() + "申请待处理");
        stringBuilder.append("姓名：").append(serviceApply.getUserName()).append("\n");
        stringBuilder.append("联系电话：").append(serviceApply.getPhoneNm()).append("\n");
        sendEmailDto.setContent(stringBuilder.toString());
        emailService.sendSimpleTextMail(sendEmailDto);
    }
}
