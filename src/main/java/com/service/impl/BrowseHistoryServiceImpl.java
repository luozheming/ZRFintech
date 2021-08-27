package com.service.impl;

import com.dto.indto.PageDto;
import com.dto.outdto.PageListDto;
import com.pojo.BrowseHistory;
import com.pojo.Investor;
import com.pojo.Project;
import com.service.BrowseHistoryService;
import com.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class BrowseHistoryServiceImpl implements BrowseHistoryService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CommonUtils commonUtils;

    @Override
    public PageListDto<BrowseHistory> pageList(PageDto pageDto) {
        int pageNum = pageDto.getPageNum();
        int pageSize = pageDto.getPageSize();

        Query query = new Query();
        query.addCriteria(where("userId").is(pageDto.getUserId()));
        int count = (int) mongoTemplate.count(query, BrowseHistory.class);

        int totalPage = count/pageSize;
        PageListDto pageListDto = new PageListDto<BrowseHistory>();
        pageListDto.setTotal(count);
        if(pageNum <= totalPage){
            int startNum = pageNum * pageSize;
            List<BrowseHistory> browseHistorys = mongoTemplate.find(query.with(Sort.by(Sort.Order.desc("updateTime"))).skip(startNum).limit(pageSize), BrowseHistory.class);
            pageListDto.setList(browseHistorys);
        }
        return pageListDto;
    }

    @Override
    public void add(BrowseHistory browseHistory) {
        browseHistory.setId(commonUtils.getNumCode());
        mongoTemplate.insert(browseHistory, "browseHistory");
    }

    @Override
    public void delete(String id) {
        mongoTemplate.remove(query(where("id").is(id)), BrowseHistory.class);
    }

    @Override
    public void clear(String userId) {
        mongoTemplate.remove(query(where("userId").is(userId)), BrowseHistory.class);
    }
}
