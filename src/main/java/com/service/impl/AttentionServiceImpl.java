package com.service.impl;

import com.dto.indto.PageDto;
import com.dto.outdto.PageListDto;
import com.pojo.Attention;
import com.pojo.Investor;
import com.pojo.Project;
import com.pojo.User;
import com.service.AttentionService;
import com.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class AttentionServiceImpl implements AttentionService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CommonUtils commonUtils;

    @Override
    public PageListDto<Attention> pageList(PageDto pageDto) {
        int pageNum = pageDto.getPageNum();
        int pageSize = pageDto.getPageSize();

        Query query = new Query();
        query.addCriteria(where("userId").is(pageDto.getUserId()));
        int count = (int) mongoTemplate.count(query, Attention.class);

        int totalPage = count/pageSize;
        PageListDto pageListDto = new PageListDto<Investor>();
        pageListDto.setTotal(count);
        if(pageNum <= totalPage){
            int startNum = pageNum * pageSize;
            List<Attention> attentions = mongoTemplate.find(query.skip(startNum).limit(pageSize), Attention.class);
            pageListDto.setList(attentions);
        }
        return pageListDto;
    }

    @Override
    public void add(Attention attention) {
        // 获取项目信息
        Project project = mongoTemplate.findOne(query(where("projectNo").is(attention.getProjectNo()).and("isDone").is(true)), Project.class);
        if (null == project) {
            return;
        }
        User user = mongoTemplate.findOne(query(where("userId").is(project.getEntUserId())), User.class);
        attention.setId(commonUtils.getNumCode());
        attention.setCreateTime(new Date());
        attention.setAttentionUserId(user.getUserId());
        attention.setAttentionCompanyName(user.getCompanyName());
        attention.setAttentionPositionName(user.getPositionName());
        attention.setAttentionRoleCode(user.getRoleCode());
        attention.setAttentionPhotoRoute(user.getPhotoRoute());
        mongoTemplate.insert(attention, "attention");
    }

    @Override
    public void cancel(Attention attention) {
        mongoTemplate.remove(query(where("userId").is(attention.getUserId()).and("attentionUserId").is(attention.getAttentionUserId())), Attention.class);
    }
}
