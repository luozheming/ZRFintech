package com.service.impl;

import com.dto.indto.PageDto;
import com.dto.outdto.PageListDto;
import com.pojo.Collection;
import com.pojo.Investor;
import com.pojo.Project;
import com.service.CollectionService;
import com.utils.CommonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CommonUtils commonUtils;

    @Override
    public PageListDto<Collection> pageList(PageDto pageDto) {
        int pageNum = pageDto.getPageNum();
        int pageSize = pageDto.getPageSize();

        Query query = new Query();
        query.addCriteria(where("userId").is(pageDto.getUserId()));
        int count = (int) mongoTemplate.count(query, Collection.class);

        int totalPage = count/pageSize;
        PageListDto pageListDto = new PageListDto<Collection>();
        pageListDto.setTotal(count);
        if(pageNum <= totalPage){
            int startNum = pageNum * pageSize;
            List<Collection> collections = mongoTemplate.find(query.skip(startNum).limit(pageSize), Collection.class);
            pageListDto.setList(collections);
        }
        return pageListDto;
    }

    @Override
    public void add(Collection collection) {
        // 查询该项目是否已被收藏
        Collection collectionResp = mongoTemplate.findOne(query(where("projectNo").is(collection.getProjectNo()).and("userId").is(collection.getUserId())), Collection.class);
        if (null != collectionResp) {
            return;
        }
        // 获取项目信息
        Project project = mongoTemplate.findOne(query(where("projectNo").is(collection.getProjectNo()).and("isDone").is(true)), Project.class);
        if (null == project) {
            return;
        }
        BeanUtils.copyProperties(project, collection);
        collection.setId(commonUtils.getNumCode());
        collection.setCreateTime(new Date());
        mongoTemplate.insert(collection, "collection");
    }

    @Override
    public void cancel(Collection collection) {
        mongoTemplate.remove(query(where("userId").is(collection.getUserId()).and("projectNo").is(collection.getProjectNo())), Collection.class);
    }
}
